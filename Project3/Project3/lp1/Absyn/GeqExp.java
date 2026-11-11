package Absyn;

public class GeqExp extends Exp
{
  public Exp left, right;

  public GeqExp(Exp left, Exp right)
  {   this.left = left;   this.right = right;   }

  public String toString()
  {   return this.left + " >= " + this.right;   }
}
