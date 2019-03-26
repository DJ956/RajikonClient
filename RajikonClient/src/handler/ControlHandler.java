package handler;

import java.io.OutputStream;
import java.io.PrintWriter;

public class ControlHandler implements IControlHandler {
	private PrintWriter writer = null;
	
	/**
	 * メイン操作通信を簡単に行うためのハンドラ
	 * @param outputStream 通信用のストリーム
	 */
	public ControlHandler(OutputStream outputStream) {
		writer = new PrintWriter(outputStream, true);
	}
	
	@Override
	public void forward(int percentage) {
		writer.println(FORWARD + percentage);
	}

	@Override
	public void back(int percentage) {
		writer.println(BACK + percentage); 
	}

	@Override
	public void left(int percentage) {
		writer.println(LEFT + percentage);
	}

	@Override
	public void right(int percentage) {
		writer.println(RIGHT + percentage);
	}

	@Override
	public void stop(int mode) {
		writer.println(STOP + mode);
	}

	@Override
	public void endChannel() {
		writer.println(END_CHANNEL);
		writer.close();
	}

	@Override
	public void endSession() {
		writer.println(END_SESSION);
		writer.close();
	}
}
