

class Vector{
	public static final Vector ZERO = new Vector(0,0,0);
	public static final Vector i = new Vector(1,0,0);
	public static final Vector j = new Vector(0,1,0);
	public static final Vector k = new Vector(0,0,1);
	
	public double x;
	public double y;
	public double z;
	
	Vector(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static Vector sum(Vector v1, Vector v2){
		return new Vector(v1.x+v2.x, v1.y+v2.y, v1.z+v2.z);
	}
	public static Vector negate(Vector v1, Vector v2){
		return new Vector(v1.x-v2.x, v1.y-v2.y, v1.z-v2.z);
	}
	public static Vector multiply(Vector v, double k){
		return new Vector(v.x*k, v.y*k, v.z*k);
	}
	public static double scalarMul(Vector v1, Vector v2){
		return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
	}
	public static Vector vectorMul(Vector v1, Vector v2){
		return new Vector(v1.y*v2.z - v1.z*v2.y, v1.z*v2.x - v1.x*v2.z, v1.x*v2.y - v1.y-v2.x);
	}
	public static double getLength(Vector v){
		return Math.sqrt(v.x*v.x + v.y*v.y + v.z*v.z);
	}
	public static Vector getOne(Vector v){
		return Vector.multiply(v, 1/Vector.getLength(v));
	}
	public static Vector rotateVector(Vector v, double a_z, double a_y){
		Vector a = new Vector(v.x, v.y, v.z);
		
		/*double l = Vector.getLength(a);
		double k = Math.cos(a_y) - v.z* Math.sin(a_y)/l;
		a = Vector.multiply(a, k);
		a.z = v.z*Math.cos(a_y) + l* Math.sin(a_y);
		
		double x = a.x;
		double y = a.y;
		a.x = x*Math.cos(a_z) + y * Math.sin(a_z);
		a.y = y*Math.cos(a_z) - x * Math.sin(a_z);*/
		
		double cos = a.x;
		double sin = a.z;
		a.x = cos*Math.cos(a_y) - sin*Math.sin(a_y);
		a.z = cos*Math.sin(a_y) + sin*Math.cos(a_y);
		
		cos = a.x;
		sin = a.y;
		a.x = cos*Math.cos(a_z) - sin*Math.sin(a_z);
		a.y = cos*Math.sin(a_z) + sin*Math.cos(a_z);
		
		return a;
	}
	public String toString(){return this.x+" "+this.y+" "+this.z;}
}

abstract class Object{
	public Vector cord;
	
	abstract public double getMinIntersection(Vector c, Vector v);
	abstract public double getMaxIntersection(Vector c, Vector v);
	abstract public Vector getNormal(Vector dote);
}


class Camera{
	public Vector cord;
	public Vector alpha;
	
	public double a_z = 0;
	public double a_y = 0;
	
	Camera(Vector c, Vector n){
		this.cord = c;
		this.alpha = n;
	}
}



class Cube extends Object{
	public double a;
	
	Cube(Vector c, double x){
		this.cord = c;
		this.a = x;
	}
	
	public double getMinIntersection(Vector c, Vector v){
		Vector d = Vector.negate(this.cord, c);
		
		double tx1 = (d.x + this.a)/v.x;
		double tx2 = (d.x - this.a)/v.x;
		if(tx1>tx2){
			double t = tx1;
			tx1 = tx2;
			tx2 = t;
		}
		double tx = tx1;
		if(tx<0) tx = tx2;
		
		double ty1 = (d.y + this.a)/v.y;
		double ty2 = (d.y - this.a)/v.y;
		if(ty1>ty2){
			double t = ty1;
			ty1 = ty2;
			ty2 = t;
		}
		double ty = ty1;
		if(ty<0) ty = ty2;
		
		double tz1 = (d.z + this.a)/v.z;
		double tz2 = (d.z - this.a)/v.z;
		if(tz1>tz2){
			double t = tz1;
			tz1 = tz2;
			tz2 = t;
		}
		double tz = tz1;
		if(tx<0) tz = tz2;
		
		double t = -1;
		if(tx>= ty1 && tx<= ty2 && tx>= tz1 && tx<= tz2 && (tx<t || t == -1)) t = tx;
		if(ty>= tx1 && ty<= tx2 && ty>= tz1 && ty<= tz2 && (ty<t || t == -1)) t = ty;
		if(tz>= ty1 && tz<= ty2 && tz>= tx1 && tz<= tx2 && (tz<t || t == -1)) t = tz;
		return t;
	}
	public double getMaxIntersection(Vector c, Vector v){
		Vector d = Vector.negate(c, this.cord);
		
		double tx1 = (d.x + this.a)/v.x;
		double tx2 = (d.x - this.a)/v.x;
		if(tx1>tx2){
			double t = tx1;
			tx1 = tx2;
			tx2 = t;
		}
		double tx = tx2;
		if(tx<0) tx = tx1;
		
		double ty1 = (d.y + this.a)/v.y;
		double ty2 = (d.y - this.a)/v.y;
		if(ty1>ty2){
			double t = ty1;
			ty1 = ty2;
			ty2 = t;
		}
		double ty = ty2;
		if(ty<0) ty = ty1;
		
		double tz1 = (d.z + this.a)/v.z;
		double tz2 = (d.z - this.a)/v.z;
		if(tz1>tz2){
			double t = tz1;
			tz1 = tz2;
			tz2 = t;
		}
		double tz = tz2;
		if(tx<0) tz = tz1;
		
		double t = -1;
		if(tx>= ty1 && tx<= ty2 && tx>= tz1 && tx<= tz2 && tx>t) t = tx;
		if(ty>= tx1 && ty<= tx2 && ty>= tz1 && ty<= tz2 && ty>t) t = ty;
		if(tz>= ty1 && tz<= ty2 && tz>= tx1 && tz<= tx2 && tz>t) t = tz;
		return t;
	}
	public Vector getNormal(Vector dote){
		if(Math.abs(dote.x - this.cord.x-this.a) < 0.01) return Vector.i;
		if(Math.abs(dote.x - this.cord.x+this.a) < 0.01) return new Vector(-1, 0,0);
		if(Math.abs(dote.y - this.cord.y-this.a)<0.01) return Vector.j;
		if(Math.abs(dote.y - this.cord.y+this.a)<0.01) return new Vector(0, -1,0);
		if(Math.abs(dote.z - this.cord.z-this.a)<0.01) return Vector.k;
		if(Math.abs(dote.z - this.cord.z+this.a)<0.01) return new Vector(0, 0,-1);
		return Vector.i;
	}
}

class Plane extends Object{
	public double d;
	
	Plane(Vector x, double y){
		this.cord = x; // normal vector
		this.d = -y;
	}
	public double getMinIntersection(Vector c, Vector v){
		double t = -(Vector.scalarMul(this.cord, c ) + d)/Vector.scalarMul(this.cord, v);
		return t;
	}
	public double getMaxIntersection(Vector c, Vector v){
		return getMinIntersection(c,v);
	}
	public Vector getNormal(Vector dote){
		return this.cord;
	}
}