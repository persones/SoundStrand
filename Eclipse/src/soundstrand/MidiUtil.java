package soundstrand;

import java.util.*;

import javax.sound.midi.*;

public class MidiUtil {

	private MidiUtil() {

	}

	public static List<MidiDevice.Info> getTransmitters() {
		ArrayList<MidiDevice.Info> transmitters = new ArrayList<MidiDevice.Info>();

		for (MidiDevice.Info mdi : MidiSystem.getMidiDeviceInfo()) {
			try {
				MidiDevice d = MidiSystem.getMidiDevice(mdi);

				if (d != null && d.getMaxTransmitters() > 0) {
					transmitters.add(mdi);
				}
			}
			catch (MidiUnavailableException e) {
				// NOP, continue
			}
		}

		return transmitters;
	}

	public static List<MidiDevice.Info> getReceivers() {
		ArrayList<MidiDevice.Info> receivers = new ArrayList<MidiDevice.Info>();

		for (MidiDevice.Info mdi : MidiSystem.getMidiDeviceInfo()) {
			try {
				MidiDevice d = MidiSystem.getMidiDevice(mdi);

				if (d != null && d.getMaxReceivers() > 0) {
					receivers.add(mdi);
				}
			}
			catch (MidiUnavailableException e) {
				// NOP, continue
			}
		}

		return receivers;
	}

}
