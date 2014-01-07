package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/* Comparable: This interface imposes a total ordering on the objects of each class that
 implements it. This ordering is referred to as the class's natural ordering,
 and the class's compareTo method is referred to as its natural comparison method.
 ---Lists (and arrays) of objects that implement this interface can be sorted automatically
 by Collections.sort (and Arrays.sort). Objects that implement this interface can be used
 as keys in a sorted map or as elements in a sorted set, without the need to specify a comparator.
 */
public class PowerSet2 {

    private String[] status;
    private List<SortedSet<Comparable>> allCombList = new ArrayList<SortedSet<Comparable>>(); //aqui vai ficar a resposta  

    public PowerSet2(String[] entrada) {
        status = entrada;
        powerSet();
    }

    public final void powerSet() {
        for (String nstatus : status) {
            allCombList.add(new TreeSet<Comparable>(Arrays.asList(nstatus))); //insiro a combinação "1 a 1" de cada item  
        }

        for (int nivel = 1; nivel < status.length; nivel++) {
            List<SortedSet<Comparable>> statusAntes = new ArrayList<SortedSet<Comparable>>(allCombList); //crio uma cópia para poder não iterar sobre o que já foi  
            for (Set<Comparable> antes : statusAntes) {
                SortedSet<Comparable> novo = new TreeSet<Comparable>(antes); //para manter ordenado os objetos dentro do set  
                novo.add(status[nivel]);
                if (!allCombList.contains(novo)) { //testo para ver se não está repetido  
                    allCombList.add(novo);
                }
            }
        }

        Collections.sort(allCombList, new Comparator<SortedSet<Comparable>>() { //aqui só para organizar a saída de modo "bonitinho"  
            @Override
            public int compare(SortedSet<Comparable> o1, SortedSet<Comparable> o2) {
                int sizeComp = o1.size() - o2.size();
                if (sizeComp == 0) {
                    Iterator<Comparable> o1iIterator = o1.iterator();
                    Iterator<Comparable> o2iIterator = o2.iterator();
                    while (sizeComp == 0 && o1iIterator.hasNext()) {
                        sizeComp = o1iIterator.next().compareTo(o2iIterator.next());
                    }
                }
                return sizeComp;
            }
        });
    }

    public ArrayList<String> getPowerSet() {
        Iterator<SortedSet<Comparable>> it = allCombList.iterator();
        ArrayList<String> saida = new ArrayList<>();
        while (it.hasNext()) {
            SortedSet<Comparable> subConjunto = it.next();
            Iterator<Comparable> elementoSubConj = subConjunto.iterator();
            String elemento = "";
            while (elementoSubConj.hasNext()) {
                elemento += (String) elementoSubConj.next();
            }
            saida.add(elemento);
        }
        return saida;
    }

    public static void main(String[] args) {
        PowerSet2 c = new PowerSet2(null);
        c.powerSet();
        c.getPowerSet();
    }
}
