package br.ufop.model;

/**
 * Created by manoelstilpen on 18/03/17.
 * @author manoelstilpen
 */

/**
 * Enum que define os niveis de jogo, alem dos possiveis estados.
 */
public enum Constantes{
    NIVEL1 {
        int valor = 1;

        @Override
        public String toString(){
            return "Nivel 1";
        }

        @Override
        public int getValor(){ return valor;}
    },

    NIVEL2{
        int valor = 2;

        @Override
        public String toString(){
            return "Nivel 2";
        }
        @Override
        public int getValor(){ return valor;}
    },

    MENU, JOGO, SAIR;

    public final static int nChances = 5;
    public int getValor(){ return 0; }
}