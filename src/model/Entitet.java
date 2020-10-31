package model;

import java.util.ArrayList;

import events.Observable;
import events.Observer;

public class Entitet extends NodeInf implements Observable {
	private String url;
	private ArrayList<Atribut> atributi;
	private ArrayList<Relacija> relacije;
	private TipEntiteta tipEntiteta;
	private ArrayList<Observer> listeners = new ArrayList<>();

	public Entitet(String name) {
		super(name);
		atributi = new ArrayList<>();
		relacije = new ArrayList<>();
	}

	public Entitet() {
		atributi = new ArrayList<>();
		relacije = new ArrayList<>();
	}

	public ArrayList<Atribut> getAtributi() {
		return atributi;
	}

	public void setAtributi(ArrayList<Atribut> atributi) {
		this.atributi = atributi;
	}

	public ArrayList<Relacija> getRelacije() {
		return relacije;
	}

	public void setRelacije(ArrayList<Relacija> relacije) {
		this.relacije = relacije;
	}

	public void dodajRelaciju(Relacija r) {
		relacije.add(r);
	}

	public void dodajAtribut(Atribut a) {
		atributi.add(a);
	}

	public void promeniAtribut(String name, int i) {
		atributi.get(i).setName(name);
	}

	public void obrisiAtribut(int i) {
		atributi.remove(i);
	}

	public void obrirsiAtribut2(Atribut a) {
		atributi.remove(a);
	}

	public void obrisiRelaciju(int i) {
		relacije.remove(i);
	}

	public void obrisiRelaciju2(Relacija r) {
		relacije.remove(r);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setTipEntiteta(TipEntiteta tipEntiteta) {
		this.tipEntiteta = tipEntiteta;
	}

	public TipEntiteta getTipEntiteta() {
		return tipEntiteta;
	}

	@Override
	public void addObserver(Observer observer) {
		listeners.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		listeners.remove(observer);
	}

	@Override
	public void notify(Object o) {
		for (Observer ob : listeners) {
			ob.update(o);
		}
	}
}
