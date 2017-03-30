package br.ufop.view;

import br.ufop.model.InfoJogo;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by manoelstilpen on 27/03/17.
 */
public class TelaHistorico extends JFrame implements Serializable {

    private static final long serialVersionUID = -900999929507564039L;

    public TelaHistorico(){

        String[] columnNames = {
                "Data",
                "Nome",
                "Jogadas",
                "Tempo",
                "Nivel"};

        ArrayList<InfoJogo> array = InfoJogo.carregaArquivo();

        Object[][] data = new Object[array.size()][5];

        int i=0;
        for(InfoJogo c : array){
            data[i][0] = c.getData();
            data[i][1] = c.getNome();
            data[i][2] = c.getnJogadas();
            data[i][3] = c.getTempo();
            data[i][4] = c.getNivel();
            i++;
        }

        JTable table = new JTable(data, columnNames);
        table.setEnabled(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);

        setTitle("Historico de Jogo");
        setLayout(new BorderLayout());
        setSize(600,400);
        setLocationRelativeTo(null);

        add(table.getTableHeader(), BorderLayout.PAGE_START);
        add(table, BorderLayout.CENTER);

        setVisible(true);
    }

}
