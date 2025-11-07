import java.util.ArrayList;
import java.util.List;

public class Ai {
    // Übergibt eine index-Instanz und ruft deren move-Methode auf
    // java

    public void ai(index idx) {
        if (idx == null) return;

        // Debug: aktuellen Zustand loggen
        System.out.println("AI debug: moveCount=" + idx.moveCount + " lastMoveDisc=" + idx.lastMoveDisc + " lastMoveSource=" + idx.lastMoveSourceTower);
        System.out.println("AI debug positions: " + idx.positions[0] + "," + idx.positions[1] + "," + idx.positions[2] + "," + idx.positions[3]);

        // 1) Sammle alle möglichen Moves (unter Beachtung der üblichen Einschränkungen)
        List<int[]> moves = new ArrayList<>();
        for (int x = 1; x <= 4; x++) {
            // Verhindere, dass die AI dieselbe Scheibe wie beim letzten Zug nochmal bewegt (primär-Regel)
            if (idx.lastMoveDisc == x) {
                System.out.println("AI skip during collect: disc " + x + " (moved last)");
                continue;
            }
            for (int y = 1; y <= 3; y++) {
                // überspringe moves auf dasselbe Ziel wie aktuell
                if (idx.positions[x - 1] == y) continue;
                // Keine direkte Umkehr der letzten Bewegung
                if (idx.lastMoveDisc == x && idx.lastMoveSourceTower == y) {
                    System.out.println("AI skip inverse during collect: disc " + x + " -> tower " + y);
                    continue;
                }
                System.out.println("AI collect try: disc " + x + " -> tower " + y);
                if (idx.checkRules(x, y)) {
                    moves.add(new int[]{x, y});
                }
            }
        }

        // Debug: Anzeige gesammelter Züge
        System.out.println("AI collected moves count: " + moves.size());
        for (int i = 0; i < moves.size(); i++) {
            int[] m = moves.get(i);
            System.out.println("  [" + i + "] disc " + m[0] + " -> tower " + m[1]);
        }

        // 2) Führe den ersten gesammelten Zug aus, wenn vorhanden
        if (!moves.isEmpty()) {
            int[] m = moves.get(0);
            System.out.println("AI: performing first collected move: disc " + m[0] + " -> tower " + m[1]);
            idx.move(m[0], m[1]);
            return;
        }

        // 3) Fallback: falls keine Moves (wegen Einschränkungen), erlauben wir jetzt auch die zuletzt bewegte Scheibe und inverse Moves
        System.out.println("AI fallback collect: no moves in primary pass, allowing last-moved disc and inverse moves");
        for (int x = 1; x <= 4; x++) {
            for (int y = 1; y <= 3; y++) {
                if (idx.positions[x - 1] == y) continue;
                System.out.println("AI fallback try: disc " + x + " -> tower " + y);
                if (idx.checkRules(x, y)) {
                    System.out.println("AI fallback: performing move disc " + x + " -> tower " + y);
                    idx.move(x, y);
                    return;
                }
            }
        }

        System.out.println("AI: no valid move found even in fallback");
    }
}