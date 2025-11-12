// TurmGen.java
import basis.*;
import java.awt.*;

/**
 * TurmGen - minimal public API wrapper around a GUI that draws three pillars and a disc.
 * Public API is intentionally small:
 *   - setPillarCenters(Point[] centers)
 *   - setDisc(Point center, double size)
 *   - updateGUI()
 *   - close()
 * Internals (window, buttons, drawing) are private.
 */
public class gui {

    // --- public API state (minimal) ---
    private Point[] pillarCenters = new Point[] {
            new Point(450, 250), // default pillar centers (x,y)
            new Point(300, 250),
            new Point(150, 250)
    };

    private double pillarSize = 100.0;

    private Point discCenter = new Point(150, 200);
    private double discSize = 20.0;

    // internal window instance
    private InnerWindow window;

    // ctor - create and show the window immediately
    public gui() {
        window = new InnerWindow();
        // initial draw
        // updateGUI();
    }

    /**
     * Set pillar centers. The array is copied. If array length < 3,
     * defaults remain for missing pillars.
     */
    public void setPillarCenters(Point[] centers) {
        if (centers == null) return;
        Point[] copy = new Point[Math.max(3, centers.length)];
        for (int i = 0; i < copy.length; i++) {
            if (i < centers.length && centers[i] != null) copy[i] = new Point(centers[i]);
            else copy[i] = (i < pillarCenters.length ? new Point(pillarCenters[i]) : new Point(100 + i * 150, 250));
        }
        this.pillarCenters = copy;
    }

    /**
     * Set the pillar "size" (used as L in original genPillar)
     */
    public void setPillarSize(double size) {
        if (size > 0) this.pillarSize = size;
    }

    /**
     * Set disc center & a logical "size" (you can adapt how size is used).
     */
    public void setDisc(Point center, double size) {
        if (center != null) this.discCenter = new Point(center);
        if (size > 0) this.discSize = size;
    }

    /**
     * Redraw the GUI with the current public state immediately.
     */
    public void updateGUI() {
        if (window != null) window.redraw(pillarCenters, pillarSize, discCenter, discSize);
    }

    /**
     * Close the window and free resources.
     */
    public void close() {
        if (window != null) {
            window.gibFrei(); // from Fenster
            window = null;
        }
    }

    // ---------------------------
    // Private inner window class
    // ---------------------------
    private static final class InnerWindow extends Fenster implements KnopfLauscher {
        private Knopf ende, knopf1, knopf2;
        private Stift stift1;

        InnerWindow() {
            initGui();
        }

        private void initGui() {
            this.setzeGroesse(600, 500);
            this.setzeTitel("TurmGen");
            ende = new Knopf("Ende", 490, 460, 100, 30);
            ende.setzeKnopfLauscher(this);
            knopf1 = new Knopf("knopf1", 10, 460, 120, 30);
            knopf1.setzeKnopfLauscher(this);
            knopf2 = new Knopf("knopf2", 150, 460, 120, 30);
            knopf2.setzeKnopfLauscher(this);
            stift1 = new Stift();
        }

        @Override
        public void bearbeiteKnopfDruck(Knopf k) {
            if (k == ende) {
                this.gibFrei();
            } else if (k == knopf1) {
                // Reserved
            } else if (k == knopf2) {
                // reserved
            }
        }

        /**
         * Public-facing redraw method (kept package-private) — called by outer class.
         */
        void redraw(Point[] pillarCenters, double pillarL, Point discCenter, double discSize) {
            // CLEAR existing drawing.
            // NOTE: replace 'clear' with the correct method name for your Stift class.
            // Many educational libraries use loesche(), loescheAlles(), or setzeZurueck().
            // If your Stift does not support a clear method, adapt here (e.g., repaint window or draw over).
            try {
                // Example attempt: if Stift exposes loescheAlles or clear
                stift1.getClass().getMethod("clear").invoke(stift1);
            } catch (Exception e1) {
                try { stift1.getClass().getMethod("loesche").invoke(stift1); } catch (Exception ex) {}
                try { stift1.getClass().getMethod("loescheAlles").invoke(stift1); } catch (Exception ex) {}
            }

            // Draw pillars
            if (pillarCenters != null) {
                for (Point pillarCenter : pillarCenters) {
                    if (pillarCenter != null) {
                        genPillar(pillarCenter.x, pillarCenter.y, (int) pillarL);
                    }
                }
            }
            // Draw disc (we ignore discSize in ellipse radii for now; you can adapt)
            if (discCenter != null) {
                genDisc(discCenter.x, discCenter.y, discSize);
            }
        }

        // --- drawing helpers copied & cleaned from original ---
        public void genPillar(int x_p, int y_p, int L) {
            stift1.bewegeAuf((x_p) - ((double) L / 2), y_p + ((double) L / 2));
            if (L > 0) {
                double[] x = { 0, L, (double) -L / 2, 0, 0 };
                double[] y = { 0, 0, 0, -L, L };
                stift1.zeichnePolygon(x, y);
            }
        }

        public void genDisc(double cx, double cy, double size) {
            double rx = 80.0 * (size / 20.0); // scale radii using size parameter
            double ry = 50.0 * (size / 20.0);
            int segments = 64;
            double[] x = new double[segments];
            double[] y = new double[segments];
            ellipsePolygonDeltas(cx, cy, rx, ry, segments, -90.0, false, x, y);
            stift1.zeichnePolygon(x, y);
        }

        public static void ellipsePolygonDeltas(double cx, double cy, double rx, double ry, int segments, double startAngleDeg, boolean clockwise, double[] x, double[] y) {
            if (segments < 3) segments = 3;
            double startRad = Math.toRadians(startAngleDeg);
            double theta = startRad;
            double prevX = cx + rx * Math.cos(theta);
            double prevY = cy + ry * Math.sin(theta);
            x[0] = 0.0;
            y[0] = 0.0;
            for (int i = 1; i < segments; i++) {
                double frac = (double) i / (double) segments;
                theta = 2.0 * Math.PI * frac;
                if (clockwise) theta = -theta;
                theta += startRad;
                double currX = cx + rx * Math.cos(theta);
                double currY = cy + ry * Math.sin(theta);
                x[i] = currX - prevX;
                y[i] = currY - prevY;
                prevX = currX;
                prevY = currY;
            }
        }
    }

    // --- Example main showing usage ---
    public static void main(String[] args) throws InterruptedException {
        gui tg = new gui();

        // move pillars and disc, then update GUI
        Point[] p = new Point[] { new Point(480, 260), new Point(300, 260), new Point(120, 260) };
        tg.setPillarCenters(p);
        tg.setPillarSize(110);
        tg.setDisc(new Point(430, 490), 12);
        tg.updateGUI();

        // keep window open; in real app you control lifecycle
        Thread.sleep(30_000);
        tg.close();
    }
}
