package org.jzy3d.plot3d.primitives;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.rendering.view.Camera;
import org.jzy3d.plot3d.transform.Transform;

public class Ellipsoid extends AbstractWireframeable {
    private float xRadius;
    private float yRadius;
    private float zRadius;
    private Coord3d center;
    private float angle;
    private Coord3d rotationVector;

    private GLUquadric qobj;
    protected int slices = 15;
    protected int stacks = 15;

    protected Color color = Color.GRAY;

    public Ellipsoid(Coord3d center, float xRadius, float yRadius,
                     float zRadius) {
        this.center = center;
        this.xRadius = xRadius;
        this.yRadius = yRadius;
        this.zRadius = zRadius;

        color.a = 0.5f;

        this.bbox = new BoundingBox3d(
                center.x - xRadius, center.x + xRadius,
                center.y - yRadius, center.y + yRadius,
                center.z - zRadius, center.z + zRadius);

        this.angle = 0;
        this.rotationVector = new Coord3d(0, 0, 0);
    }

    public Ellipsoid(Coord3d center, float xRadius, float yRadius,
                     float zRadius, float angle, Coord3d rotationVector) {
        this(center, xRadius, yRadius, zRadius);
        this.angle = angle;
        this.rotationVector = rotationVector;
    }

    @Override
    public void draw(GL gl, GLU glu, Camera camera) {
        GL2 gl2 = gl.getGL2();
        if(transform!=null)
            transform.execute(gl);

        gl2.glTranslatef(center.x,center.y,center.z);
        gl2.glScalef(1.0f, yRadius/xRadius, zRadius/xRadius);
        gl2.glRotatef(angle, rotationVector.x, rotationVector.y, rotationVector.z);

        if(qobj==null)
            qobj = glu.gluNewQuadric();

        if(facestatus) {
            gl2.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_FILL);
            gl2.glColor4f(color.r, color.g, color.b, color.a);
            glu.gluSphere(qobj, xRadius, slices, stacks);
            //glut.glutSolidSphere(radius, slices, stacks);
        }
        if(wfstatus){
            gl2.glPolygonMode(GL.GL_FRONT_AND_BACK, GL2.GL_LINE);
            gl.glLineWidth(wfwidth);
            gl2.glColor4f(wfcolor.r, wfcolor.g, wfcolor.b, wfcolor.a);
            glu.gluSphere(qobj, xRadius, slices, stacks);
            //glut.glutSolidSphere(radius, slices, stacks);
        }
    }

    @Override
    public void applyGeometryTransform(Transform transform) {

    }

    @Override
    public void updateBounds() {

    }
}
