package a;

import org.apache.commons.lang.SerializationUtils;

public class TestServiceImpl implements TestService {
	public Para doSomething(Para p) {
		// showClassLoader();
		return p;
	}

	public Para doSomething(byte[] buf) {
		showClassLoader();
		return (Para) SerializationUtils.deserialize(buf);
	}

	public byte[] doSomething2(byte[] buf) {
		//showClassLoader();
		//System.out.println(new String(buf));
		//System.out.println("ddd"+this.getClass().hashCode());
		return SerializationUtils.serialize(buf);
	}

	private void showClassLoader() {
		System.out.println("getContextClassLoader="
				+ Thread.currentThread().getContextClassLoader());
		System.out
				.println("getClassLoader=" + this.getClass().getClassLoader());
	}
}
