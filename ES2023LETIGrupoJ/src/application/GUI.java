package application;

import java.awt.Dimension;
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
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.opencsv.exceptions.CsvException;

import carregamento_de_horario.HorarioLoader;
import carregamento_de_horario.Horario_ISCTE;
import carregamento_de_horario.ListaSalasLoader;
import carregamento_de_horario.ListaSalas_ISCTE;
import carregamento_de_horario.SaveFiles;
import mapeamento.ColumnOrderDialog;
import mapeamento.Mapeamento;
import qualidade_dos_horarios.Metricas;

/**
 * Classe que representa a interface gráfica da aplicação. Permite carregar
 * horários, realizar mapeamento e gerenciar métricas.
 */
public class GUI extends JFrame {

	private static Horario_ISCTE horarioISCTE;
	private static ListaSalas_ISCTE salasISCTE;
	private static String diretorioEscolhido;

	/**
	 * Retorna o diretório escolhido para carregamento de horário.
	 * 
	 * @return Diretório escolhido.
	 */
	public static String getDiretorioEscolhido() {
		return diretorioEscolhido;
	}

	/**
	 * Construtor da classe GUI. Inicializa as instâncias de Horario_ISCTE e
	 * ListaSalas_ISCTE.
	 */
	public GUI() {
		super("A Minha Aplicacao");
		horarioISCTE = new Horario_ISCTE();
		salasISCTE = new ListaSalas_ISCTE();
		diretorioEscolhido = null;
	}

	/**
	 * Método para criar e exibir a interface gráfica.
	 */
	public void createAndShowGUI() {
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("CARREGAMENTO_HORARIO", createTab1Content());
		tabbedPane.addTab("MAPEAMENTO", createTab3Content());
		tabbedPane.addTab("GESTAO_DAS_METRICAS", createTab2Content());

		add(tabbedPane);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(530, 400);
		setVisible(true);
	}

	/**
	 * Método para criar o conteúdo da primeira aba (CARREGAMENTO_HORARIO).
	 * 
	 * @return JPanel com o conteúdo da aba.
	 */
	private static JPanel createTab1Content() {
		JPanel tab1Panel = new JPanel();
		JButton button = new JButton("Carregar Horario");
		JButton button2 = new JButton("Carregar novo Horario");
		button.setToolTipText(
				"Carregar horario a partir de um ficheiro CSV e visualiza-lo no browser web, sob a forma de tabela");
		button.setBounds(20, 20, 250, 250);
		button2.setBounds(20, 20, 250, 250);

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
							Object[] options = { "Horario", "Lista de Salas" };
							int choice = JOptionPane.showOptionDialog(null, "Escolha qual conteúdo HTML visualizar:",
									"Escolha de Conteudo", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
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

									fileChooser.setDialogTitle("Selecione o ficheiro CSV do horario");
									fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos CSV", "csv"));

									int result = fileChooser.showOpenDialog(null);

									if (result == JFileChooser.APPROVE_OPTION) {
										String horarioCsvFilePath = fileChooser.getSelectedFile().getAbsolutePath();

										horarioISCTE.setHorarioCsvFilePath(horarioCsvFilePath);

										diretorioEscolhido = fileChooser.getSelectedFile().getParent();

										horarioISCTE.carregarHorario(horarioCsvFilePath);

										tab1Panel.add(button2);

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

											button.setText("Alterar visualizacao");

											// Escolher qual conteúdo HTML visualizar
											Object[] options = { "Horario", "Lista de Salas" };
											int choice = JOptionPane.showOptionDialog(null,
													"Escolha qual conteudo HTML visualizar:", "Escolha de Conteudo",
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

		button2.addActionListener(new ActionListener() {
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

									if (horarioISCTE.getHorarioCsvFilePath().equals(csvFilePath)) {
										// Se os caminhos forem iguais, exibe um aviso e retorna
										JOptionPane.showMessageDialog(null,
												"O novo ficheiro selecionado já se encontra carregado na aplicação",
												"Aviso", JOptionPane.WARNING_MESSAGE);
										return null;
									}

									horarioISCTE.setHorarioCarregado(false);
									// Se os caminhos não forem iguais, continua o restante do método
									horarioISCTE.carregarHorario(csvFilePath);

									String htmlContent = HorarioLoader.loadHorarioFromCSV(csvFilePath);

									SaveFiles.salvarArquivoHTML(htmlContent);
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

		return tab1Panel;
	}

	/**
	 * Método para criar o conteúdo da segunda aba (GESTAO_DAS_METRICAS).
	 * 
	 * @return JPanel com o conteúdo da aba.
	 */
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

					String htmlContent = Metricas.loadHorarioFromCSV_button1(horarioISCTE, caracterizacaoSalasMap);

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

					String htmlContent = Metricas.loadHorarioFromCSV_button3(horarioISCTE);

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

					String htmlContent = Metricas.loadHorarioFromCSV_button4(horarioISCTE);

					SaveFiles.salvarArquivoHTML(htmlContent);

				} catch (IOException | CsvException ex) {
					ex.printStackTrace();
				}
			}
		});

		tab2Panel.add(newbutton4);

		return tab2Panel;
	}

	/**
	 * Método para criar o conteúdo da terceira aba (MAPEAMENTO).
	 * 
	 * @return JPanel com o conteúdo da aba.
	 */
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

					List<String> originalOrder = horarioISCTE.getColumnOrder();

					if (!customOrder.equals(originalOrder)) {
						JOptionPane.showMessageDialog(null, "O mapeamento dos campos foi mal executado", "Aviso",
								JOptionPane.WARNING_MESSAGE);
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
