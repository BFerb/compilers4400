package Absyn;

public class EquExp extends Exp
{
  public Exp left, right;

  public EquExp(Exp left, Exp right)
  {   this.left = left;   this.right = right;   }

  public String toString()
  {   return this.left + " == " + this.right;   }
}
