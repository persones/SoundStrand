package soundstrand;

import javax.sound.midi.Sequencer;

public class CurserUpdaterThread extends Thread {

	Sequencer sequencer;
	StrandView strandView;


	@Override
	public void run() {
		while(sequencer.isRunning()) {
			SequencePreparer sp = (SequencePreparer)sequencer.getSequence();
			strandView.setCurserLocation(sequencer.getTickPosition() + (sp.getMotifIndex() * GlobalParams.BAR_LENGTH * GlobalParams.PPQ));
		}
	}
	public Sequencer getS() {
		return sequencer;
	}
	public void setS(Sequencer s) {
		this.sequencer = s;
	}
	public StrandView getSv() {
		return strandView;
	}
	public void setSv(StrandView sv) {
		this.strandView = sv;
	}
}




