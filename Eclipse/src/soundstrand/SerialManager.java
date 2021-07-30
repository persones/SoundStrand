
package soundstrand;


/**
 *
 * @author Person
 */

import java.io.InputStream;
//import java.io.OutputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 

import java.util.ArrayList;
import java.util.Enumeration;

public class SerialManager implements SerialPortEventListener {
	SerialPort serialPort;
        /** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
			"/dev/ttyUSB0", // Linux
			"COM4", // Windows
			};
		/** The port we're normally going to use. */
	
	/** Buffered input stream from the port */
	private InputStream input;
	/** The output stream to the port */
	//private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;
	
	public SerialManager() {
		initialize();
		sliderList = new ArrayList<javax.swing.JSlider>();
	}
	
	public void assignStrand(Strand inStrand) {
		strand = inStrand;
	}

	public void initialize() {

		CommPortIdentifier portId = null;
		@SuppressWarnings("rawtypes")
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}

		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = serialPort.getInputStream();
			//output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
			portActive = true;
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
	
	public void setMode(boolean inMode) {
		mode = inMode;
	}
	
	public boolean isPortActive() {
		return portActive;
	}
	
	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				int available = input.available();
				byte[] chunk = new byte[available];
				input.read(chunk, 0, available);
				// Displayed results are codepage dependent
				
				if (mode) {
					for (int charIndex = 0; charIndex < available; charIndex++) {
						short c = chunk[charIndex];
						//System.out.printf("%d ",(short)(256 + c));
						if (c < 0) {
							c = (short)(256 + c);
						}
						if (END_OF_TRANMISSION == c) {
							cubeIndex = 0;
							for (int i = cubes.size() - 1; i >= 0; i--) {
								strand.setMotifProerties(cubeIndex, cubes.get(i).id, cubes.get(i).potList);
								cubeIndex++;
							}				
							strand.cutTailToFitLength(cubeIndex);
							cubeIndex = -1;
							potNo = -1;
							cubes = new ArrayList<CubeParams>();
						//	System.out.println("");
							
						}
						else if ((ID_MSG & c) > 0) {
							cubeIndex++;
							cubes.add(new CubeParams());
							cubes.get(cubeIndex).id = (ID_MSG ^ c);
							potNo = 0;
						}
						else { // TODO : validate cube
							if ((potNo < 3) && (cubeIndex >= 0)) {
								cubes.get(cubeIndex).potList[potNo++] = 127 - c;
						
							}	
						}
					}
				}				
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}
	
	public void addSlider(javax.swing.JSlider inSlider) {
		sliderList.add(inSlider);
	}
	private ArrayList<javax.swing.JSlider> sliderList;
	private static final short ID_MSG = 0x80;
	private static final short END_OF_TRANMISSION = 0xFE;
	private int cubeIndex = -1;
	private int potNo = -1;
	private Strand strand;
	private boolean mode = false;
	private boolean portActive = false;
	private ArrayList<CubeParams> cubes = new ArrayList<CubeParams>();
}
