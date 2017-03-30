package br.ufop.control;

import br.ufop.model.Constantes;
import br.ufop.view.TelaInicial;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by manoelstilpen on 18/03/17.
 */
public class ControlInicio {

    private final String caminhoEntradaSom = "arquivos/sons/entrada.wav";

    private String nome;
    private Constantes nivel;

    private AudioInputStream ais;
    private Clip audio;

    private TelaInicial telaInicial = new TelaInicial();

    private Thread t_tela;

    public ControlInicio(){

    }

    public Constantes run(){
        initAudio();
        telaInicial.setControl(this);
        executaTelaInicial();

        return Constantes.JOGO;
    }

    public void initAudio(){

        try {

            audio = AudioSystem.getClip();
            ais = AudioSystem.getAudioInputStream(new File(caminhoEntradaSom));

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public void executaTelaInicial(){

        // INICIA A THREAD QUE EXECUTA A TELA INICIAL
        t_tela = new Thread() {
            public void run() {

                telaInicial.setVisible(true);
                boolean sair = false;
                   
                audio.setFramePosition(0);
                audio.start();

                while (!sair) {
                    // aguarda ate a tela inicial for fechada
                    if (!telaInicial.isVisible()) {
                        sair = true;
                    }

                    try{
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                audio.close();
            }
        };

        t_tela.start();

        try {
            // aguarda o termino da tela inicial
            t_tela.join();
            t_tela.interrupt();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Constantes getNivel() {
        return nivel;
    }

    public void setNivel(Constantes nivel) {
        this.nivel = nivel;
    }
}
