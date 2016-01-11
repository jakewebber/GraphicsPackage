import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

public class FrameSnap {
     
     public static void main(String[] args) {
          
          JFrame frame = new JFrame();
          frame.setSize(500, 500);
          frame.setLocation(50,50);
          frame.setVisible(true);
          
          JDesktopPane desk = new JDesktopPane();
          JInternalFrame snapFrame = new JInternalFrame();
          snapFrame.setSize(200, 200);
          snapFrame.setVisible(true);
          snapFrame.setResizable(true);
          snapFrame.setLocation(100,100);

          snapFrame.addComponentListener(new ComponentAdapter() {
               public void componentResized(ComponentEvent e) {
                    JInternalFrame frame = (JInternalFrame) e.getSource();

                    int snapSize = 20;
                    int width = frame.getWidth();
                    int height = frame.getHeight();

                    //Round off resized dimensions to snap size
                    width = ((width + snapSize/2)/snapSize) * snapSize;
                    height = ((height + snapSize/2)/snapSize) * snapSize;

                    //Snaps resize to implicit grid, but doesn't
                    //work right if resizing to the left or up
                    frame.setSize(width, height);
               }
          });
          
          desk.add(snapFrame);
          frame.add(desk);

     }

}