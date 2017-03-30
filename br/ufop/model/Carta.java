package br.ufop.model;

import javax.swing.*;

import br.ufop.model.Constantes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by manoelstilpen on 18/03/17.
 * @author manoelstilpen
 */
@SuppressWarnings("serial")
public class Carta extends JLabel {

    private int id_carta;
    private int img_code;

    private String caminho_imagem;

    public Carta(){
        esconder();
    }

    /**
     *
     * @param id_carta id da carta
     * @param id_img id da imagem: usada para iniciar o icone
     */
    public Carta(int id_carta, int id_img){
        this.id_carta = id_carta;

        setImgCode(id_img);
        esconder();

    }

    /**
     *
     * @param o o objeto que sera comparado
     * @return true se a instancia do objeto for a mesma e false caso contrario
     */
    @Override
    public boolean equals(Object o) {

        if(o == null){
          return false;

        } else if (o == this) {
            return true;

        }

        return false;
    }

    public int getIdCarta() {
        return id_carta;
    }

    public void setImgCode(int c){
        this.img_code = c;
        setCaminhoImagem(c);
    }

    /**
     * Recebe o código da imagem e converte no seu caminho no sistema
     * @param code codigo da imagem
     */
    public void setCaminhoImagem(int code) {
        this.caminho_imagem = "/img/cartas/" + Integer.toString(code) + ".png";
    }

    /**
     * Muda o icone, exibindo na tela o naipe
     */
    public void mostrar(){
        this.setIcon(new ImageIcon(getClass().getResource(this.caminho_imagem)));
    }

    /**
     * Muda o icone, escondendo o naipe e mostrando a parte de tras da carta
     */
    public void esconder(){
        this.setIcon(new ImageIcon(getClass().getResource("/img/cartas/tras.png")));
    }

}
