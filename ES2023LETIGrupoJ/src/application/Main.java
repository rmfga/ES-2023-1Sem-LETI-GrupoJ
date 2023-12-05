package application;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.opencsv.exceptions.CsvException;

import carregamento_de_horário.HorarioLoader;
import carregamento_de_horário.Horario_ISCTE;
import mapeamento.ColumnOrderDialog;
import mapeamento.Mapeamento;
import qualidade_dos_horários.Métricas;

public class Main {

	private static Horario_ISCTE horarioISCTE;

	public static void main(String[] args) {
		horarioISCTE = new Horario_ISCTE();

		SwingUtilities.invokeLater(() -> createAndShowGUI());
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("A Minha Aplicação");

		JTabbedPane tabbedPane = new JTabbedPane();

		JPanel tab1Panel = createTab1Content();
		tabbedPane.addTab("CARREGAMENTO_HORÁRIO", tab1Panel);

		JPanel tab2Panel = createTab2Content();
		tabbedPane.addTab("GESTÃO_DAS_MÉTRICAS", tab2Panel);

		JPanel tab3Panel = createTab3Content();
		tabbedPane.addTab("MAPEAMENTO", tab3Panel);

		frame.add(tabbedPane);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(530, 400);
		frame.setVisible(true);
	}

	private static JPanel createTab1Content() {
		JPanel tab1Panel = new JPanel();
		JButton button = new JButton("Carregar Horário");
		button.setBounds(20, 20, 250, 250);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					SwingWorker<Void, Void> worker = new SwingWorker<>() {
						@Override
						protected Void doInBackground() {
							try {
								JFileChooser fileChooser = new JFileChooser();
								fileChooser.setDialogTitle("Selecione o arquivo CSV do horário");
								fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos CSV", "csv"));

								int result = fileChooser.showOpenDialog(null);

								if (result == JFileChooser.APPROVE_OPTION) {
									String csvFilePath = fileChooser.getSelectedFile().getAbsolutePath();
									horarioISCTE.carregarHorario(csvFilePath);

									String htmlContent = HorarioLoader.loadHorarioFromCSV(csvFilePath);

									// Definir o nome do arquivo HTML
									String htmlFileName = "SalasDeAulaPorTiposDeSala.html";

									fileChooser.setDialogTitle("Selecione o local para salvar o arquivo HTML");
									fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos HTML", "html"));

									// Configurar o nome do arquivo e o diretório
									fileChooser.setSelectedFile(new File(htmlFileName));

									result = fileChooser.showSaveDialog(null);

									if (result == JFileChooser.APPROVE_OPTION) {
										// Obter o caminho completo do arquivo
										String htmlFilePath = fileChooser.getSelectedFile().getAbsolutePath();
										HorarioLoader.saveHTMLToFile(htmlFilePath, htmlContent);
										htmlFilePath = htmlFilePath.replace("\\", "/");
										Desktop desk = Desktop.getDesktop();
										desk.browse(new java.net.URI("file://" + htmlFilePath));
										
									}
								}
							} catch (IOException | URISyntaxException | CsvException ex) {
								ex.printStackTrace();
							}
							return null;
						}
					};

					worker.execute();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		tab1Panel.add(button);

		return tab1Panel;
	}

	private static JPanel createTab2Content() {
		JPanel tab2Panel = new JPanel();

		JButton newbutton1 = new JButton("Salas Sobrelotadas");
		newbutton1
				.setToolTipText("Mostrar as Aulas com Salas Sobrelotadas e quantos Alunos estão a mais no Browser Web");

		JButton newbutton2 = new JButton("Características Desperdiçadas");
		newbutton2.setToolTipText(
				"Mostrar as Características desperdiçadas nas Salas atribuídas às aulas no Browser Web");

		JButton newbutton3 = new JButton("Aulas sem Características");
		newbutton3.setToolTipText(
				"Mostrar Aulas que são Realizadas em Salas que não têm as Características Solicitadas no Browser Web");

		JButton newbutton4 = new JButton("Aulas sem Sala");
		newbutton4.setToolTipText("Mostrar Aulas em que não foram Atribuídas Salas no Browser Web");

		newbutton1.setBounds(20, 20, 400, 50);
		newbutton2.setBounds(20, 90, 400, 50);
		newbutton3.setBounds(20, 160, 400, 50);
		newbutton4.setBounds(20, 230, 400, 50);

		newbutton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!horarioISCTE.isHorarioCarregado()) {
					JOptionPane.showMessageDialog(null, "Por favor, carregue o horário primeiro.", "Aviso",
	                        JOptionPane.WARNING_MESSAGE);
	                return;
				}

				try {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Selecione o arquivo CSV do horário");
					fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos CSV", "csv"));

					int result = fileChooser.showOpenDialog(null);

					if (result == JFileChooser.APPROVE_OPTION) {
						String horarioFilePath = fileChooser.getSelectedFile().getAbsolutePath();

						fileChooser.setDialogTitle("Selecione o arquivo CSV das salas");
						result = fileChooser.showOpenDialog(null);

						if (result == JFileChooser.APPROVE_OPTION) {
							String caracterizacaoSalasFilePath = fileChooser.getSelectedFile().getAbsolutePath();

							// Processa o arquivo CaracterizaçãoDasSalas.csv e cria o mapa associativo
							Map<String, Integer> caracterizacaoSalasMap = Métricas
									.processarCaracterizacaoDasSalas(caracterizacaoSalasFilePath);

							// Conteúdo HTML
							String htmlContent = Métricas.loadHorarioFromCSV_button1(horarioFilePath,
									caracterizacaoSalasMap);

							// Nome do arquivo HTML
							String htmlFileName = "SalasDeAulaPorTiposDeSala.html";

							// Configurar o local para salvar o arquivo HTML
							fileChooser.setDialogTitle("Selecione o local para salvar o arquivo HTML");
							fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos HTML", "html"));
							fileChooser.setSelectedFile(new File(htmlFileName));

							result = fileChooser.showSaveDialog(null);

							if (result == JFileChooser.APPROVE_OPTION) {
								String htmlFilePath = fileChooser.getSelectedFile().getAbsolutePath();
								Métricas.saveHTMLToFile(htmlFilePath, htmlContent);

								Desktop desk = Desktop.getDesktop();
								desk.browse(new java.net.URI("file://" + htmlFilePath));
							}
						}
					}
				} catch (IOException | URISyntaxException | CsvException ex) {
					ex.printStackTrace();
				}
			}
		});

		tab2Panel.add(newbutton1);

		newbutton3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (!horarioISCTE.isHorarioCarregado()) {
					JOptionPane.showMessageDialog(null, "Por favor, carregue o horário primeiro.", "Aviso",
	                        JOptionPane.WARNING_MESSAGE);
	                return;
				}
				
				try {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Selecione o arquivo CSV do horário");
					fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos CSV", "csv"));

					int result = fileChooser.showOpenDialog(null);

					if (result == JFileChooser.APPROVE_OPTION) {
						String csvFilePath = fileChooser.getSelectedFile().getAbsolutePath();

						// Conteúdo HTML
						String htmlContent = Métricas.loadHorarioFromCSV_button3(csvFilePath);

						// Nome do arquivo HTML
						String htmlFileName = "SalasDeAulaPorTiposDeSala.html";

						// Configurar o local para salvar o arquivo HTML
						fileChooser.setDialogTitle("Selecione o local para salvar o arquivo HTML");
						fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos HTML", "html"));
						fileChooser.setSelectedFile(new File(htmlFileName));

						result = fileChooser.showSaveDialog(null);

						if (result == JFileChooser.APPROVE_OPTION) {
							String htmlFilePath = fileChooser.getSelectedFile().getAbsolutePath();
							Métricas.saveHTMLToFile(htmlFilePath, htmlContent);

							Desktop desk = Desktop.getDesktop();
							desk.browse(new java.net.URI("file://" + htmlFilePath));
						}
					}
				} catch (IOException | URISyntaxException | CsvException ex) {
					ex.printStackTrace();
				}
			}
		});

		tab2Panel.add(newbutton3);

		newbutton4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (!horarioISCTE.isHorarioCarregado()) {
					JOptionPane.showMessageDialog(null, "Por favor, carregue o horário primeiro.", "Aviso",
	                        JOptionPane.WARNING_MESSAGE);
	                return;
				}
				
				try {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Selecione o arquivo CSV do horário");
					fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos CSV", "csv"));

					int result = fileChooser.showOpenDialog(null);

					if (result == JFileChooser.APPROVE_OPTION) {
						String csvFilePath = fileChooser.getSelectedFile().getAbsolutePath();

						// Conteúdo HTML
						String htmlContent = Métricas.loadHorarioFromCSV_button4(csvFilePath);

						// Nome do arquivo HTML
						String htmlFileName = "SalasDeAulaPorTiposDeSala.html";

						// Configurar o local para salvar o arquivo HTML
						fileChooser.setDialogTitle("Selecione o local para salvar o arquivo HTML");
						fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos HTML", "html"));
						fileChooser.setSelectedFile(new File(htmlFileName));

						result = fileChooser.showSaveDialog(null);

						if (result == JFileChooser.APPROVE_OPTION) {
							String htmlFilePath = fileChooser.getSelectedFile().getAbsolutePath();
							Métricas.saveHTMLToFile(htmlFilePath, htmlContent);

							Desktop desk = Desktop.getDesktop();
							desk.browse(new java.net.URI("file://" + htmlFilePath));
						}
					}

				} catch (IOException | URISyntaxException | CsvException ex) {
					ex.printStackTrace();
				}
			}
		});

		tab2Panel.add(newbutton4);

		return tab2Panel;
	}

	private static JPanel createTab3Content() {
		JPanel tab3Panel = new JPanel();

		JButton button = new JButton("Ordem");
		button.setBounds(20, 20, 250, 50);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (!horarioISCTE.isHorarioCarregado()) {
					JOptionPane.showMessageDialog(null, "Por favor, carregue o horário primeiro.", "Aviso",
	                        JOptionPane.WARNING_MESSAGE);
	                return;
				}
				
				try {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Selecione o arquivo CSV do horário");
					fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos CSV", "csv"));

					int result = fileChooser.showOpenDialog(null);

					if (result == JFileChooser.APPROVE_OPTION) {
						String csvFilePath = fileChooser.getSelectedFile().getAbsolutePath();

						// Exibir janela para o usuário definir a ordem das colunas
						String[] columnNames = Mapeamento.readColumnNames(csvFilePath);
						ColumnOrderDialog dialog = new ColumnOrderDialog(null, columnNames);
						dialog.setVisible(true);

						List<String> customOrder = dialog.getCustomOrder();
						if (customOrder.isEmpty()) {
							System.out.println("Operação de seleção de ordem personalizada cancelada.");
							return;
						}

						// Nome do arquivo HTML
						String htmlFileName = "SalasDeAulaPorTiposDeSala.html";

						// Configurar o local para salvar o arquivo HTML
						fileChooser.setDialogTitle("Selecione o local para salvar o arquivo HTML");
						fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos HTML", "html"));
						fileChooser.setSelectedFile(new File(htmlFileName));

						result = fileChooser.showSaveDialog(null);

						if (result == JFileChooser.APPROVE_OPTION) {
							String htmlFilePath = fileChooser.getSelectedFile().getAbsolutePath();
							String htmlContent = Mapeamento.loadHorarioFromCSV_CustomOrder(csvFilePath, customOrder);
							Mapeamento.saveHTMLToFile(htmlFilePath, htmlContent);

							Desktop desk = Desktop.getDesktop();
							desk.browse(new java.net.URI("file://" + htmlFilePath));
						}
					}

				} catch (IOException | URISyntaxException ex) {
					ex.printStackTrace();
				}
			}
		});

		tab3Panel.add(button);

		return tab3Panel;
	}

}
