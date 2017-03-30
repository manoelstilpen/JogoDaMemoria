package br.ufop.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import br.ufop.control.ControlGame;
import br.ufop.model.Constantes;
import br.ufop.model.Carta;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by manoelstilpen on 20/03/17.
 * @author manoelstilpen
 */
@SuppressWarnings("serial")
public class TelaJogo extends JFrame {

    private ArrayList<Carta> cartas;
    private Constantes nivel;
    private ControlGame control;
    private int nCartas;

    private JPanel panelLeft;
    private JPanel panelCenter;

    private JButton btHelp;

    private JLabel labelAjuda;
    private JLabel labelTempo;
    private JLabel labelJogadas;

    public TelaJogo(){}

    /**
     *
     * @param nivel nivel de jogo escolhido: usado para iniciar tamanho da tela
     * @param nCartas quantidade de cartas que vai ser utilizada, dependente do nivel escolhido
     */
    public TelaJogo(Constantes nivel, int nCartas){
        setNivel(nivel);
        setnCartas(nCartas);
    }

    /**
     * Desenha os componentes na tela e exibe
     */
    public void draw(){
        createUIComponents();
        setVisible(true);
    }

    /**
     * Funcao onde é inserido os componentes e onde é implementado os listeners
     */
    private void createUIComponents() {

        setLayout(new BorderLayout(10,10));
        setTitle("Jogo da Memoria");
        setVisible(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // gambiarra pra inserir imagem no fundo
        setLayout(new FlowLayout());
        setContentPane(new JLabel(new ImageIcon(getClass().getResource("/img/background.jpg"))));
        setLayout(new BorderLayout(30,10));

        // regiao esquerda e centro terao um painel separado
        panelLeft = new JPanel(new GridLayout(0,1));
        panelLeft.setOpaque(false);
        panelLeft.setBorder(new EmptyBorder(3,10,10,3));

        panelCenter = new JPanel();
        panelCenter.setOpaque(false);

        // inicia layout para inserir no centro com as cartas
        GridLayout gridLayout = new GridLayout();
        if(nivel == Constantes.NIVEL1){
            // para menos cartas, usa uma tela menor, de tamanho fixo
            gridLayout.setColumns(4);
            gridLayout.setRows(0);
            setSize(800, 600);
        } else {
            // mais cartas usa uma tela grande
            gridLayout.setColumns(6);
            gridLayout.setRows(0);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setUndecorated(true);
        }

        // adiciona layout no painel central
        panelCenter.setLayout(gridLayout);
        panelCenter.setEnabled(false);

        // adiciona as cartas na tela
        for(Carta c : cartas){
            panelCenter.add(c);
        }

        // evento para quando houver clique numa carta
        panelCenter.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // verificacao para realizar cast
                if (panelCenter.getComponentAt(e.getPoint()) instanceof Carta && control.isCliqueHabilitado()) {

                    // inicia worker para virar carta
                    SwingWorker worker = new SwingWorker() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            Carta carta = (Carta) panelCenter.getComponentAt(e.getPoint());
                            control.cartaClicada(carta);
                            return null;
                        }
                    };

                    worker.execute();
                }
            }
        });

        labelTempo = new JLabel("00:00");
        labelTempo.setForeground(Color.WHITE);
        labelTempo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTempo.setFont(new Font("sans", Font.BOLD, 26));

        labelJogadas = new JLabel("0 Jogada");
        labelJogadas.setForeground(Color.WHITE);
        labelJogadas.setHorizontalAlignment(SwingConstants.CENTER);
        labelJogadas.setFont(new Font("sans", Font.BOLD, 24));

        // botao de ajuda
        btHelp = new JButton(new ImageIcon(getClass().getResource("/img/help-icon.png")));
        btHelp.setToolTipText("Clique uma vez para virar todas as cartas");
        btHelp.setOpaque(false);
        btHelp.setContentAreaFilled(false);
        btHelp.setBorderPainted(false);
        btHelp.setBorder(null);

        JPanel panelAjuda = new JPanel(new GridLayout(0,1));
        panelAjuda.setOpaque(false);

        labelAjuda = new JLabel("");
        labelAjuda.setText("Ajudas: " + control.getnChances());
        labelAjuda.setForeground(Color.WHITE);
        labelAjuda.setHorizontalAlignment(SwingConstants.CENTER);
        labelAjuda.setFont(new Font("sans", Font.BOLD, 24));

        btHelp.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                SwingWorker worker = new SwingWorker() {
                    @Override
                    protected Object doInBackground() throws Exception {
                        control.botaoAjuda();
                        labelAjuda.setText("Ajudas: " + control.getnChances());
                        return null;
                    }
                };

                worker.execute();
            }
        });

        panelAjuda.add(labelAjuda);
        panelAjuda.add(btHelp);

        panelLeft.add(labelTempo);
        panelLeft.add(labelJogadas);
        panelLeft.add(panelAjuda);

        add(panelCenter, BorderLayout.CENTER);
        add(panelLeft, BorderLayout.LINE_START);
    }

    public void atualizaCronometro(String t){
        labelTempo.setText(t);
    }

    public void atualizaJogadas(int n){
        labelJogadas.setText(n + " Jogadas");
    }

    public void setCartas(ArrayList<Carta> cartas) {
        this.cartas = cartas;
    }

    public void setNivel(Constantes nivel) {
        this.nivel = nivel;
    }

    public void setnCartas(int nCartas) {
        this.nCartas = nCartas;
    }

    public void setControl(ControlGame control) {
        this.control = control;
    }

    public void disableHelp(){
        btHelp.setEnabled(false);
    }


}
