package moduleExamples.openCV;

public class HelloOpenCV {
	  public static void main(String[] args) {
	    System.out.println("Hello, OpenCV");

	    // Load the native library.
	    System.loadLibrary("opencv_java245");
	    new DetectFaceDemo().run();
	  }
	}