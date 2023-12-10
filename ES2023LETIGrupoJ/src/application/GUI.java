package application;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.opencsv.exceptions.CsvException;
import carregamento_de_horário.HorarioLoader;
import carregamento_de_horário.Horario_ISCTE;
import carregamento_de_horário.ListaSalasLoader;
import carregamento_de_horário.ListaSalas_ISCTE;
import carregamento_de_horário.SaveFiles;
import mapeamento.ColumnOrderDialog;
import mapeamento.Mapeamento;
import qualidade_dos_horários.Métricas;

public class GUI extends JFrame {

	private static Horario_ISCTE horarioISCTE;
	private static ListaSalas_ISCTE salasISCTE;
	private static String diretorioEscolhido;

	public static String getDiretorioEscolhido() {
		return diretorioEscolhido;
	}

	public GUI() {
		super("A Minha Aplicação");
		horarioISCTE = new Horario_ISCTE();
		salasISCTE = new ListaSalas_ISCTE();
		diretorioEscolhido = null;
	}

	public void createAndShowGUI() {
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("CARREGAMENTO_HORÁRIO", createTab1Content());
		tabbedPane.addTab("MAPEAMENTO", createTab3Content());
		tabbedPane.addTab("GESTÃO_DAS_MÉTRICAS", createTab2Content());
		

		add(tabbedPane);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(530, 400);
		setVisible(true);
	}

	private static JPanel createTab1Content() {
		JPanel tab1Panel = new JPanel();
		JButton button = new JButton("Carregar Horário");
		button.setToolTipText(
				"Carregar horário a partir de um ficheiro CSV e visualizá-lo no browser web, sob a forma de tabela");
		button.setBounds(20, 20, 250, 250);

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {

					if (horarioISCTE.isHorarioCarregado() && salasISCTE.isSalasCarregada()) {

						int userChoice = JOptionPane.showConfirmDialog(null,
								"Se pretende alterar o ficheiro a visualizar, clique em 'Yes'. Se pretende sair, clique em 'No'.",
								"Aviso", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

						if (userChoice == JOptionPane.NO_OPTION) {
							return; // O usuário escolheu não continuar
						} else {
							Object[] options = { "Horário", "Lista de Salas" };
							int choice = JOptionPane.showOptionDialog(null, "Escolha qual conteúdo HTML visualizar:",
									"Escolha de Conteúdo", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
									null, options, options[0]);

							String htmlContent;
							if (choice == 0) {
								htmlContent = horarioISCTE.getHtmlContent();
								SaveFiles.salvarArquivoHTML(htmlContent);
							} else {
								htmlContent = salasISCTE.getHtmlContent();
								SaveFiles.salvarArquivoHTML(htmlContent);

							}
						}
					}

					SwingWorker<Void, Void> worker = new SwingWorker<>() {
						@Override
						protected Void doInBackground() {
							try {

								if (!horarioISCTE.isHorarioCarregado() || !salasISCTE.isSalasCarregada()) {
									JFileChooser fileChooser = new JFileChooser();

									fileChooser.setDialogTitle("Selecione o ficheiro CSV do horário");
									fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos CSV", "csv"));

									int result = fileChooser.showOpenDialog(null);

									if (result == JFileChooser.APPROVE_OPTION) {
										String horarioCsvFilePath = fileChooser.getSelectedFile().getAbsolutePath();

										diretorioEscolhido = fileChooser.getSelectedFile().getParent();

										horarioISCTE.carregarHorario(horarioCsvFilePath);

										JFileChooser salasFileChooser = new JFileChooser();
										salasFileChooser.setDialogTitle("Selecione o ficheiro CSV das salas");

										// Configura o diretório inicial
										if (getDiretorioEscolhido() != null) {
											salasFileChooser.setCurrentDirectory(new File(getDiretorioEscolhido()));
										}

										salasFileChooser
												.setFileFilter(new FileNameExtensionFilter("Arquivos CSV", "csv"));

										result = salasFileChooser.showOpenDialog(null);

										// Restante do seu código...

										if (result == JFileChooser.APPROVE_OPTION) {
											String salasCsvFilePath = salasFileChooser.getSelectedFile()
													.getAbsolutePath();

											salasISCTE.carregarListaSalas(salasCsvFilePath);

											button.setText("Alterar visualização");

											// Escolher qual conteúdo HTML visualizar
											Object[] options = { "Horário", "Lista de Salas" };
											int choice = JOptionPane.showOptionDialog(null,
													"Escolha qual conteúdo HTML visualizar:", "Escolha de Conteúdo",
													JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
													options, options[0]);

											String htmlContent;
											if (choice == 0) {
												htmlContent = HorarioLoader.loadHorarioFromCSV(horarioCsvFilePath);
												SaveFiles.salvarArquivoHTML(htmlContent);
											} else {
												htmlContent = ListaSalasLoader.loadHorarioFromCSV(salasCsvFilePath);
												SaveFiles.salvarArquivoHTML(htmlContent);
											}
										}

									}
								}
							} catch (IOException | CsvException ex) {
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

		newbutton1.setPreferredSize(new Dimension(250, 30));
		newbutton2.setPreferredSize(new Dimension(250, 30));
		newbutton3.setPreferredSize(new Dimension(250, 30));
		newbutton4.setPreferredSize(new Dimension(250, 30));

		newbutton1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (!horarioISCTE.isHorarioCarregado()) {
					JOptionPane.showMessageDialog(null, "Por favor, carregue o horário primeiro.", "Aviso",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				try {
					Map<String, Integer> caracterizacaoSalasMap = salasISCTE.getSalaMap();

					String htmlContent = Métricas.loadHorarioFromCSV_button1(horarioISCTE, caracterizacaoSalasMap);

					SaveFiles.salvarArquivoHTML(htmlContent);

				} catch (IOException | CsvException ex) {
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

					String htmlContent = Métricas.loadHorarioFromCSV_button3(horarioISCTE);

					SaveFiles.salvarArquivoHTML(htmlContent);

				} catch (IOException | CsvException ex) {
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

					String htmlContent = Métricas.loadHorarioFromCSV_button4(horarioISCTE);

					SaveFiles.salvarArquivoHTML(htmlContent);

				} catch (IOException | CsvException ex) {
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
					// Exibir janela para o usuário definir a ordem das colunas

					ColumnOrderDialog dialog = new ColumnOrderDialog(null, horarioISCTE);

					dialog.setVisible(true);

					List<String> customOrder = dialog.getCustomOrder();
					if (customOrder.isEmpty()) {
						System.out.println("Operação de seleção de ordem personalizada cancelada.");
						return;
					}

					String htmlContent = Mapeamento.loadHorarioFromCSV_CustomOrder(horarioISCTE, customOrder);
					
					horarioISCTE.carregarHorario(htmlContent);

					SaveFiles.salvarArquivoHTML(htmlContent);

				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (CsvException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		tab3Panel.add(button);

		return tab3Panel;
	}

}
