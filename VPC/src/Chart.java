import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import javax.swing.JPanel;

public class Chart extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final int MIN_BAR_WIDTH = 4;
    private Map<Integer, Integer> mapHistory;

    public Chart(Map<Integer, Integer> mapHistory) {
        this.mapHistory = mapHistory;
        int width = (mapHistory.size() * MIN_BAR_WIDTH) + 11;
        Dimension minSize = new Dimension(width, 128);
        Dimension prefSize = new Dimension(width, 256);
        setMinimumSize(minSize);
        setPreferredSize(prefSize);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mapHistory != null) {
            int xOffset = 5;
            int yOffset = 5;
            int width = getWidth() - 1 - (xOffset * 2);
            int height = getHeight() - 1 - (yOffset * 2);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawRect(xOffset, yOffset, width, height);
            int barWidth = Math.max(MIN_BAR_WIDTH,
                    (int) Math.floor((float) width
                    / (float) mapHistory.size()));
            System.out.println("width = " + width + "; size = "
                    + mapHistory.size() + "; barWidth = " + barWidth);
            int maxValue = 0;
            for (Integer key : mapHistory.keySet()) {
                int value = mapHistory.get(key);
                maxValue = Math.max(maxValue, value);
            }
            int xPos = xOffset;
            for (Integer key : mapHistory.keySet()) {
                int value = mapHistory.get(key);
                int barHeight = Math.round(((float) value
                        / (float) maxValue) * height);
                g2d.setColor(new Color(key, key, key));
                int yPos = height + yOffset - barHeight;
//Rectangle bar = new Rectangle(xPos, yPos, barWidth, barHeight);
                Rectangle2D bar = new Rectangle2D.Float(
                        xPos, yPos, barWidth, barHeight);
                g2d.fill(bar);
                g2d.setColor(Color.DARK_GRAY);
                g2d.draw(bar);
                xPos += barWidth;
            }
            g2d.dispose();
        }
    }
}


