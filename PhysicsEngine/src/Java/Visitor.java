package Java;

public interface Visitor {
    public void visit(ControlMod control);
    public void visit(EnviroMod enviro);
    public void visit(CollideMod collide);


}
