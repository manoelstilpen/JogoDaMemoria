package br.ufop.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

import java.util.Comparator;
import java.util.Collections;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


/**
 * Created by manoelstilpen on 26/03/17.
 * @author manoelstilpen
 */
public class InfoJogo implements Serializable, Comparable<InfoJogo>{

    private static final long serialVersionUID = 5830814365875949087L;
    private static final String nomeArquivo = "arquivos/historico";

    private int nJogadas;
    private int nSegundos;
    private int nMinutos;

    private Constantes nivel;

    private Date data; // usado para armazenar a data e hora do jogo

    private String nome;

    public InfoJogo(String nome, Constantes nivel){
        this.nome = nome;
        this.nivel = nivel;
        this.nJogadas = 0;
        this.nSegundos = 0;
        this.nMinutos = 0;

        this.data = new Date();
    }

    /**
     * Sobrescrevendo funcao da interface Comparable
     */
    @Override
    public int compareTo(InfoJogo c1) {
        //return this.nivel.getValor() - c1.getNivel().getValor();
        return this.getnJogadas() - c1.getnJogadas();
    }

    /**
     * Adiciona o tempo em um segundo
     * @return o tempo em formato string
     */
    public String adicionaTempo(){
        nSegundos++;

        if(nSegundos > 59){
            nMinutos++;
            nSegundos = 0;
        }

        return getStrTempo();
    }

    public String getTempo(){
        return getStrTempo();
    }

    /**
     *
     * @return o tempo percorrido em formato string
     */
    private String getStrTempo(){
        String strSegundos = Integer.toString(nSegundos);
        if(strSegundos.length() == 1){
            strSegundos = "0" + strSegundos;
        }

        String strMinutos = Integer.toString(nMinutos);
        if(strMinutos.length() == 1){
            strMinutos = "0" + strMinutos;
        }

        return strMinutos + ":" + strSegundos;
    }

    public void salvar(){
        // verifica se o arquivo esta vazio ou nao, caso esteja vazio e passado 0 como quantidade de objetos

        try {
            FileInputStream input = new FileInputStream(nomeArquivo);
            if(input.read() == -1){
                salvaEmArquivo(0);
            } else {
                int n = getNumObjetosNoArquivo();
                salvaEmArquivo(n);
            }

            input.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metodo que realiza a leitura de todos os objetos no arquivo, concatena com o objeto this, e logo, salva
     * todos novamente no arquivo.
     *
     * @param nObjects recebe a quantidade de objetos que estao alocados no arquivo
     */
    private void salvaEmArquivo(int nObjects) {

        ArrayList<InfoJogo> array = new ArrayList<>();
        ObjectOutputStream output;

        try {

            // leitura dos objetos que ja foram salvos
            if(nObjects != 0)
                array = carregaArquivo();

            // insere o objeto atual no array
            array.add(this);

            Collections.sort (array, new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    InfoJogo p1 = (InfoJogo) o1;
                    InfoJogo p2 = (InfoJogo) o2;
                    return p1.compareTo(p2);
                }
            });

            output = new ObjectOutputStream(new FileOutputStream(nomeArquivo));
            output.writeInt(nObjects+1);

            // salva os objetos
            for(InfoJogo info : array)
                output.writeObject(info);

            output.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ArrayList<InfoJogo> carregaArquivo(){

        ArrayList<InfoJogo> array = new ArrayList<>();
        ObjectInputStream input = null;

        int nObjects = getNumObjetosNoArquivo();
        
        if(nObjects != 0) {
            try {
                input = new ObjectInputStream(new FileInputStream(nomeArquivo));

                input.readInt();

                int i = 0;
                while (i < nObjects) {
                    // armazena numa lista os objetos ja anteriormente alocados
                    Object o = input.readObject();
                    if (o instanceof InfoJogo) {
                        InfoJogo aux = (InfoJogo) o;
                        array.add(aux);
                    }
                    i++;
                }

                input.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return array;
    }

    /**
     *
     * @return a quantidade de objetos que estao alocados no arquivo
     */
    private static int getNumObjetosNoArquivo(){

        ObjectInputStream objectInputStream;
        int nObjects = 0;

        try {

            objectInputStream = new ObjectInputStream(new FileInputStream(nomeArquivo));
            nObjects = objectInputStream.readInt();

            objectInputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return nObjects;
    }

    @Override
    public String toString(){
        return "Nome: " + nome + " Jogadas: " + nJogadas + " " + nMinutos + ":" + nSegundos + "s Nivel: " + nivel;
    }

    public void adicionarJogada(){
        this.nJogadas++;
    }

    public int getnJogadas() {
        return nJogadas;
    }

    public void setnJogadas(int nJogadas) {
        this.nJogadas = nJogadas;
    }

    public int getnSegundos() {
        return nSegundos;
    }

    public void setnSegundos(int nSegundos) {
        this.nSegundos = nSegundos;
    }

    public int getnMinutos() {
        return nMinutos;
    }

    public void setnMinutos(int nMinutos) {
        this.nMinutos = nMinutos;
    }

    public Constantes getNivel() {
        return nivel;
    }

    public void setNivel(Constantes nivel) {
        this.nivel = nivel;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(this.data);
    }

}
