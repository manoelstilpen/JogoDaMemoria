package br.ufop.control;

import br.ufop.model.*;
import br.ufop.view.TelaJogo;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by manoelstilpen on 20/03/17.
 * @author manoelstilpen
 */
public class ControlGame{

    private final String caminhoErroSom = "arquivos/sons/erro.wav";
    private final String caminhoAcertoSom = "arquivos/sons/acerto.wav";
    private final String caminhoAjudaSom = "arquivos/sons/ajuda.wav";
    private final String caminhoFinalSom = "arquivos/sons/venceu.wav";
    private final String caminhoAplausosSom = "arquivos/sons/aplausos.wav";

    private ArrayList<Carta> cartas;
    private Constantes nivel;
    private Constantes estadoRetorno = Constantes.SAIR;

    private String nomeJogador;

    private int nCartas;
    private int nCartasAtivas = 0;
    private int nChances = Constantes.nChances;

    private InfoJogo infoJogo;

    private boolean cliqueHabilitado = true;

    private ArrayList<Carta> cartasAtivas = new ArrayList<Carta>(2);

    private TelaJogo telaJogo = new TelaJogo();

    private AudioInputStream aisErro;
    private AudioInputStream aisAcerto;
    private AudioInputStream aisPalmas;
    private AudioInputStream aisAjuda;
    private AudioInputStream aisFinal;
    private Clip audioErro;
    private Clip audioAcerto;
    private Clip audioPalmas;
    private Clip audioAjuda;
    private Clip audioFinal;

    public ControlGame(){

    }

    /**
     *
     * @param name nome do jogador
     * @param nivel nivel de jogo
     */
    public ControlGame(String name, Constantes nivel){
        setPlayerName(name);
        setNivel(nivel);
    }

    /**
     * Funcao que inicia o jogo, envia os parametros necessarios para a tela.
     * Inicia uma thread que mantem um loop ate que a tela seja fechada.
     */

    public void initAudio(){
        try {

            aisErro = AudioSystem.getAudioInputStream(new File(caminhoErroSom));
            audioErro = AudioSystem.getClip();
            audioErro.open(aisErro);

            aisAcerto = AudioSystem.getAudioInputStream(new File(caminhoAcertoSom));
            audioAcerto = AudioSystem.getClip();
            audioAcerto.open(aisAcerto);

            aisPalmas = AudioSystem.getAudioInputStream(new File(caminhoAplausosSom));
            audioPalmas = AudioSystem.getClip();
            audioPalmas.open(aisPalmas);
            
            aisAjuda = AudioSystem.getAudioInputStream(new File(caminhoAjudaSom));
            audioAjuda = AudioSystem.getClip();
            audioAjuda.open(aisAjuda);

            aisFinal = AudioSystem.getAudioInputStream(new File(caminhoFinalSom));
            audioFinal = AudioSystem.getClip();
            audioFinal.open(aisFinal);

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public Constantes run(){
        initAudio();

        infoJogo = new InfoJogo(nomeJogador, nivel);

        telaJogo.setNivel(nivel);
        telaJogo.setnCartas(nCartas);
        telaJogo.setControl(this);
        iniciaCartas();
        telaJogo.setCartas(cartas);

        telaJogo.draw();
            
        audioPalmas.setFramePosition(0);
        audioPalmas.start();

        Thread t = new Thread() {
            public void run() {
                boolean sairTela = false;

                while (!sairTela) {
                    if (!telaJogo.isVisible()) {
                        sairTela = true;
                    }

                    telaJogo.atualizaCronometro(infoJogo.adicionaTempo());

                    try {
                        // delay de atualizacao do cronometro
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        try {
            t.start();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return estadoRetorno;

    }

    /**
     * Funcao que recebe a referencia para a carta clicada e faz os tratamentos devidos.
     * Verifica se houve acerto nas cartas escolhidas
     *
     * @param c recebe a carta que foi clicada na tela
     */
    public void cartaClicada(Carta c){
        // enquanto estiver realizando processamento, o clique em outras cartas é desabilitado
        cliqueHabilitado = false;

        if(nCartasAtivas == 1 && !cartasAtivas.get(0).equals(c)){

            cartasAtivas.add(1, c);
            Thread t1 = new Thread(() -> { cartasAtivas.get(1).mostrar();});

            t1.start();
            // aguarda thread que vira a carta terminar a execucao
            try {
                t1.join();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // verifica se houve acerto nas cartas
            if(cartasAtivas.get(0).getIdCarta() == cartasAtivas.get(1).getIdCarta() &&
                    !cartasAtivas.get(0).equals(cartasAtivas.get(1))) {
                    //cartasAtivas.get(0) != cartasAtivas.get(1)){

                audioAcerto.setFramePosition(0);
                audioAcerto.start();

                for(Carta carta : cartasAtivas){
                    carta.desabilita();
                }
                nCartas--;

                if(nCartas == 0){
                    finalizaJogo();
                }
            
            } else {
                audioErro.setFramePosition(0);
                audioErro.start();               
            }

            esconderAtivas();
            cartasAtivas.clear();
            nCartasAtivas = 0;

            infoJogo.adicionarJogada();
            telaJogo.atualizaJogadas(infoJogo.getnJogadas());

        } else if (nCartasAtivas == 0) {
            c.mostrar();
            cartasAtivas.add(0,c);
            nCartasAtivas++;
        }

        cliqueHabilitado = true;

    }

    /**
     * Funcao que é chamada quando o botão de ajuda é clicado.
     * Vira as cartas e verifica se o numero de chances ainda e maior que 0
     */
    public void botaoAjuda(){

        cliqueHabilitado = false;

        if(nChances > 0) {

            Thread t1 = new Thread(() -> {
            
                audioAjuda.setFramePosition(0);
                audioAjuda.start();

            });

            t1.start();
            mostrarTodas();

            try {
                t1.join();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            esconderTodas();

            nChances--;

            if(nChances == 0){
                telaJogo.disableHelp();
            }
        }

        cliqueHabilitado = true;
    }

    /**
     * Vira todas as cartas, deixando todos os naipes a vista
     */
    public void mostrarTodas(){
        for(Carta c : cartas){
            c.mostrar();
        }
    }

    /**
     * Vira para baixo aquelas cartas que foram clicadas
     */
    public void esconderAtivas(){
        for(Carta c : cartasAtivas){
            c.esconder();
        }
    }

    /**
     * Vira para baixo todas as cartas
     */
    public void esconderTodas(){
        for(Carta c : cartas){
            c.esconder();
        }
    }

    /**
     * Quando é decretada a vitoria. Realiza uma chamada no sistema com comando de voz
     */
    public void finalizaJogo(){

        audioFinal.setFramePosition(0);
        audioFinal.start();

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        infoJogo.salvar();

        estadoRetorno = Constantes.MENU;
        telaJogo.setVisible(false);
    }

    /**
     * Inicia a quantidade de cartas e as cartas em si, considerando o nivel de jogo escolhido.
     * Ao final, embaralha o vetor
     */
    public void iniciaCartas(){

        if(nivel == Constantes.NIVEL1){
            nCartas = 8;
        } else {
            nCartas = 18;
        }

        this.cartas = new ArrayList<Carta>(nCartas);

        for(int i=0 ; i < nCartas ; i++){
            cartas.add(new Carta(i, i+1));
            cartas.add(new Carta(i, i+1));
        }

        Collections.shuffle(cartas);
    }


    public void setNivel(Constantes n){
        this.nivel = n;
    }

    public void setPlayerName(String n){
        this.nomeJogador = n;
    }

    public int getnChances() {
        return nChances;
    }

    public synchronized boolean isCliqueHabilitado() {
        return cliqueHabilitado;
    }


}