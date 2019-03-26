package handler;

public interface IControlHandler {
	
	String END_SESSION = "END_SESSION";
	String END_CHANNEL = "END_CHANNEL";
	
	String FORWARD = "Forward:";
	String BACK = "Back:";
	String LEFT = "Left:";
	String RIGHT = "Right:";
	String STOP = "Stop:";
	
	int STOP_RIGHT = 0;
	int STOP_LEFT = 1;
	int STOP_BOTH = 2;

	void endChannel();
	void endSession();
	
	void forward(int percentage);
	void back(int percentage);
	void left(int percentage);
	void right(int percentage);
	void stop(int mode);
}
