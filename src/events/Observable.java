package events;


// interfejs za listener
public interface Observable
{
	void addObserver(Observer observer);
	void removeObserver(Observer observer);
	void notify(Object o);
}
