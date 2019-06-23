package es.ucm.fdi.tp.view.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public abstract class PlayerInfoTable extends JPanel{

	private MyTableModel tModel;

	protected Map<Integer, Color> colors; // Line -> Color
	protected Map<Integer, Color> colorsB;
	protected ColorChooser colorChooser;
	
	public PlayerInfoTable() {
		initGUI();
	}

	private void initGUI() {
		this.setLayout(new BorderLayout());
		  this.setBorder(BorderFactory.createTitledBorder("Player Information"));
		colors = new HashMap<>();
		colorsB = new HashMap<>();
		colors.put(0, Color.RED);
		colors.put(1, Color.BLUE);
		colorsB.put(0, Color.BLACK);
		colorsB.put(1, Color.LIGHT_GRAY);
		colorChooser = new ColorChooser(new JFrame(), "Choose Line Color", Color.BLACK);

		// names table
		tModel = new MyTableModel();
		tModel.getRowCount();
		JTable table = new JTable(tModel) {

			// THIS IS HOW WE CHANGE THE COLOR OF EACH ROW
			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component comp = super.prepareRenderer(renderer, row, col);

				// the color of row 'row' is taken from the colors table, if
				// 'null' setBackground will use the parent component color.
				if (col == 1)
					comp.setBackground(colors.get(row));
				else if (col == 2)
					comp.setBackground(colorsB.get(row));
				else
					comp.setBackground(Color.WHITE);
				comp.setForeground(Color.BLACK);
				return comp;
			}
		};

		table.setToolTipText("Click on a row to change the color of a player");
		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = table.rowAtPoint(evt.getPoint());
				int col = table.columnAtPoint(evt.getPoint());
				if (row >= 0 && col == 1) {
					changeColor(row);
				}
				else if (row >= 0 && col == 2) {
					changeColorBackground(row);
				}
			}

		});

		this.add(new JScrollPane(table), BorderLayout.CENTER);
		

		this.add(new JLabel("Click on a row, in the table above, to change its background color"), BorderLayout.PAGE_END);
		
		this.setOpaque(true);
		this.setPreferredSize(new Dimension(100,100));
		this.setMinimumSize(new Dimension(50,50));
	}

	protected abstract void changeColor(int row);
	protected abstract void changeColorBackground(int row);
	public Color getColor(int player){
		return colors.get(player);
	}
	public Color getColorBG(int row, int col){
		int num = (row+col) % 2;
		return colorsB.get(num);
	}
}
