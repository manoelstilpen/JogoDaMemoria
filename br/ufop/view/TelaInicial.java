package br.ufop.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import br.ufop.control.ControlInicio;
import br.ufop.model.Constantes;

/**
 * Created by manoelstilpen on 13/03/17.
 * @author manoelstilpen
 */
@SuppressWarnings("serial")
public class TelaInicial extends JFrame{

    private JLabel labelNome;

    private JButton btJogar;

    private JTextField txtName;

    private ImageIcon imagemFundo;

    private ButtonGroup radioButtonGroup;
    private JRadioButton btNivel1, btNivel2;

    JMenuBar menuBar;
    JMenuItem menuHistorico;

    ControlInicio control;

    public TelaInicial(){

        this.setLayout(new GridLayout(2,1));
        this.setVisible(false);

        menuBar = new JMenuBar();
        menuHistorico = new JMenuItem("Historico");
        menuHistorico.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TelaHistorico();
            }
        });


        menuBar.add(menuHistorico);

        imagemFundo = new ImageIcon(getClass().getResource("/img/capa.jpg"));

        JPanel panelInicio = new JPanel();
        panelInicio.setLayout(new BoxLayout(panelInicio, BoxLayout.Y_AXIS));
        panelInicio.setBounds(250,200, 250, 75);

        labelNome = new JLabel("<html><font color='red'>Qual seu nome?</font></html>", JLabel.CENTER);
        labelNome.setFont(new Font("Sans", Font.PLAIN, 20));
        labelNome.setBounds(300, 250, 200, 75);
        labelNome.setVisible(true);

        txtName = new JTextField(20);
        txtName.setText("");
        txtName.setFont(new Font("Sans", Font.PLAIN, 20));
        txtName.setVisible(true);
        txtName.setHorizontalAlignment(JTextField.CENTER);
        txtName.setBounds(300,300,200,25);

        panelInicio.add(labelNome);
        panelInicio.add(txtName);

        btNivel1 = new JRadioButton("Nivel 1");
        btNivel1.setBounds(250, 300, 100, 30);
        btNivel1.setHorizontalAlignment(SwingConstants.CENTER);
        btNivel1.addActionListener(event -> control.setNivel(Constantes.NIVEL1));

        btNivel2 = new JRadioButton("Nivel 2");
        btNivel2.setBounds(400, 300, 100, 30);
        btNivel2.setHorizontalAlignment(SwingConstants.CENTER);
        btNivel2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                control.setNivel(Constantes.NIVEL2);
            }
        });

        radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(btNivel1);
        radioButtonGroup.add(btNivel2);

        btJogar = new JButton(new ImageIcon(getClass().getResource("/img/playButton.jpg")));
        btJogar.setHorizontalTextPosition(JButton.CENTER);
        btJogar.setBounds(300,375,150,112);
        btJogar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if((btNivel1.isSelected() || btNivel2.isSelected()) && !txtName.getText().equals("")){
                    control.setNome(txtName.getText());
                    setVisible(false);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos!", "ERRO", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        this.setTitle("Jogo de Memória");
        this.setSize(800, 600);
        this.setContentPane(new JLabel(imagemFundo));
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.pack();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setJMenuBar(menuBar);
        this.add(panelInicio);
        this.add(btNivel1);
        this.add(btNivel2);
        this.add(btJogar);
    }

    public Constantes run(){
        setVisible(true);

        return Constantes.JOGO;
    }

    public Constantes getNivel(){
        return control.getNivel();
    }

    public String getPlayerName(){
        return control.getNome();
    }

    public void setControl(ControlInicio control) {
        this.control = control;
    }

}
