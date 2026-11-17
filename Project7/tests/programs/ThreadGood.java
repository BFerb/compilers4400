/**
 * COSC 4400 - Project #7
 * Provides a translator regression test.
 * @authors Nick Grons, Ben Ferber
 * Instructor Dr. Brylow
 * TA-BOT:MAILTO nicholas.grons@marquette.edu benjamin.ferber@marquette.edu
 */
class ThreadGood
{
	public static void main(String[] args)
	{
		int f;

		Thread1 myThread1 = new Thread1();
		Thread2 myThread2 = new Thread2();
		Data ddd = new Data();
		Data tempD;

		f = ddd.set_field(0);

		tempD = myThread1.setData(ddd);
		tempD = myThread2.setData(ddd);

		Xinu.threadCreate(myThread2);
		Xinu.threadCreate(myThread1);

	}
}


class Thread1 extends Thread
{
	Data d;
	int x;

	public void run()
	{
		x = d.increment_one();
		x = d.field_print();

	}

	public Data setData(Data dd)
	{
		d = dd;
		return d;
	}
}


class Thread2 extends Thread
{
	Data d;
	int x;

	public void run()
	{
		x = d.increment_ten();
		x = d.field_print();

	}

	public Data setData(Data dd)
	{
		d = dd;
		return d;
	}
}


class Data
{
	int field;

	public int set_field(int x)
	{
		field = x;
		return field;
	}

	public synchronized int increment_one()
	{
		int temp = field;
		temp = temp + 1;
		temp = this.write(temp);
		
		return field;
	}

	public synchronized int increment_ten()
	{
		int temp = field;
		temp = temp + 10;
		temp = this.write(temp);

		return field;
	}
	public synchronized int write(int x)
	{
		//yield
		Xinu.yield();
		field = x;

		return field;
	}

	public synchronized int field_print()
	{
		int temp = field;

		//yield
		Xinu.yield();

		Xinu.print("field = ");
		Xinu.printint(temp);

		return field;
	}
}













