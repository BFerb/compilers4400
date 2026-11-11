package Absyn;

public class BoolLiteral extends Exp
{
  public boolean b;

  public BoolLiteral(boolean b)
  {  this.b = b;   } 
     

  public String toString()
  {   return Boolean.toString(b);   }
}
