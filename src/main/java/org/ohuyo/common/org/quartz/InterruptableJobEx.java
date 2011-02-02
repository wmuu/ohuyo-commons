package org.ohuyo.common.org.quartz;

import org.quartz.InterruptableJob;
import org.quartz.UnableToInterruptJobException;

/**
 * ¿ÉÖÐ¶ÏJob
 * 
 * @author rabbit
 * 
 */
public abstract class InterruptableJobEx extends JobEx implements
		InterruptableJob {
	private class InterruptFlagImpl implements InterruptFlag {
		private volatile boolean flag = false;

		public boolean interrupted() {
			return flag;
		}

		private void interrupt() {
			flag = true;
		}
	}

	private final InterruptFlagImpl interruptFlag = new InterruptFlagImpl();

	public final void interrupt() throws UnableToInterruptJobException {
		interruptFlag.interrupt();
		interruptEx();
	}

	final protected InterruptFlag getInterruptFlag() {
		return interruptFlag;
	}

	abstract protected void interruptEx() throws UnableToInterruptJobException;

}
