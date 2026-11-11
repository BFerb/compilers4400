package Absyn;

public class NeqExp extends Exp
{
  public Exp left, right;

  public NeqExp(Exp left, Exp right)
  {   this.left = left;   this.right = right;   }

  public String toString()
  {   return this.left + " != " + this.right;   }
}
