package managers;

import javax.ejb.Singleton;

@Singleton
public class LogBean implements LogLocal,LogRemote{

	@Override
	public void info(String message) {
		// TODO Auto-generated method stub
		System.out.println(message);
	}

	@Override
	public void error(String message) {
		// TODO Auto-generated method stub
		System.err.println(message);
	}

}
