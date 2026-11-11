package Absyn;

public class LeqExp extends Exp
{
  public Exp left, right;

  public LeqExp(Exp left, Exp right)
  {   this.left = left;   this.right = right;   }

  public String toString()
  {   return this.left + " <= " + this.right;   }
}
