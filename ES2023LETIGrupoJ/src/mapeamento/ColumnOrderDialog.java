package mapeamento;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.border.EmptyBorder;

public class ColumnOrderDialog extends JDialog {
	private DefaultListModel<String> listModel;
	private JList<String> columnList;
	private JButton okButton;

	public ColumnOrderDialog(Frame parent, String[] columnNames) {
		super(parent, "Definir Ordem das Colunas", true);
		listModel = new DefaultListModel<>();

		for (String columnName : columnNames) {
			listModel.addElement(columnName);
		}

		columnList = new JList<>(listModel);
		columnList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Adicione suporte a arrastar e soltar
		columnList.setDragEnabled(true);
		columnList.setDropMode(DropMode.INSERT);
		columnList.setTransferHandler(new ListTransferHandler());

		JScrollPane scrollPane = new JScrollPane(columnList);
		scrollPane.setPreferredSize(new Dimension(400, 400));

		okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		mainPanel.add(okButton, BorderLayout.SOUTH);

		setContentPane(mainPanel);
		pack();
		setLocationRelativeTo(parent);

		// Adicione um ouvinte de mouse para selecionar itens com um clique
		columnList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					JList list = (JList) e.getSource();
					int index = list.locationToIndex(e.getPoint());
					list.setSelectedIndex(index);
				}
			}
		});
	}

	public List<String> getCustomOrder() {
		List<String> customOrder = new ArrayList<>();
		for (int i = 0; i < listModel.size(); i++) {
			customOrder.add(listModel.getElementAt(i));
		}
		return customOrder;
	}

	// Handler para transferência de dados
	private class ListTransferHandler extends TransferHandler {
		@Override
		public int getSourceActions(JComponent c) {
			return TransferHandler.MOVE;
		}

		@Override
		protected Transferable createTransferable(JComponent c) {
			JList<String> list = (JList<String>) c;
			int index = list.getSelectedIndex();
			String value = list.getSelectedValue();
			return new StringSelection(value);
		}

		@Override
		protected void exportDone(JComponent source, Transferable data, int action) {
			if (action == TransferHandler.MOVE) {
				JList<String> list = (JList<String>) source;
				int index = list.getSelectedIndex();
				listModel.remove(index);
			}
		}

		@Override
		public boolean canImport(TransferHandler.TransferSupport support) {
			return support.isDataFlavorSupported(DataFlavor.stringFlavor);
		}

		@Override
		public boolean importData(TransferHandler.TransferSupport support) {
			try {
				String value = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
				JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
				int index = dl.getIndex();
				listModel.add(index, value);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}
}
