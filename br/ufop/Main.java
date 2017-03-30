package br.ufop;

import br.ufop.control.ControlGame;
import br.ufop.control.ControlInicio;
import br.ufop.model.Constantes;

public class Main {

    public static void main(String[] args) {

        ControlInicio controlInicio = new ControlInicio();
        ControlGame controlGame = new ControlGame();

        Constantes decisao;

        do {
            decisao = controlInicio.run();

            if(decisao == Constantes.JOGO){
                controlGame.setNivel(controlInicio.getNivel());
                controlGame.setPlayerName(controlInicio.getNome());
                decisao = controlGame.run();
            }

        }while(decisao != Constantes.SAIR);

        System.exit(0);
    }
}
