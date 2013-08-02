package rajawali.bounds.volumes;

import java.util.concurrent.atomic.AtomicInteger;

import rajawali.BaseObject3D;
import rajawali.Camera;
import rajawali.Geometry3D;
import rajawali.math.Plane;
import rajawali.math.Plane.PlaneSide;
import rajawali.math.Quaternion;
import rajawali.math.vector.Vector3;
import rajawali.primitives.NPrism;
import rajawali.primitives.Sphere;
import rajawali.util.RajLog;

public class CameraFrustum implements IBoundingVolume {
	
	protected Sphere mVisualSphere;
	protected float[] mTmpMatrix = new float[16];
	protected AtomicInteger mBoundingColor = new AtomicInteger(IBoundingVolume.DEFAULT_COLOR);
	public final Plane[] mPlanes = new Plane[6];     
	
	protected Camera mCamera;
	protected CameraVisibleFrustum mVisibleFrustum;
	protected final Quaternion mOrientation = new Quaternion();
	private static final Quaternion s90_DEGREE = new Quaternion(Vector3.X, -90.0f);
	
	protected static final double ROOT2_4 = Math.sqrt(2.0)/4.0;

	protected final Vector3[] mPlanePoints = { 
			new Vector3(), new Vector3(), new Vector3(), new Vector3(), 
			new Vector3(), new Vector3(), new Vector3(), new Vector3() 
	}; 
	
	public Vector3 getPlanePoint(int index) {
		return mPlanePoints[index];
	}
	
	protected static final Vector3[] mClipSpacePlanePoints = { 
		new Vector3(-1, -1, -1), 
		new Vector3( 1, -1, -1), 
		new Vector3( 1,  1, -1), 
		new Vector3(-1,  1, -1), 
		new Vector3(-1, -1,  1), 
		new Vector3( 1, -1,  1), 
		new Vector3( 1,  1,  1),
		new Vector3(-1,  1,  1)};
	
	public CameraFrustum(Camera cam) {
		mCamera = cam;
		for(int i = 0; i < 6; i++) {
			mPlanes[i] = new Plane(new Vector3(), 0);
		}
	}

	public void update(float[] inverseProjectionView) {             
		for(int i = 0; i < 8; i++) {
			mPlanePoints[i].setAll(mClipSpacePlanePoints[i]);
			mPlanePoints[i].project(inverseProjectionView);   
		}

		mPlanes[0].set(mPlanePoints[1], mPlanePoints[0], mPlanePoints[2]);
		mPlanes[1].set(mPlanePoints[4], mPlanePoints[5], mPlanePoints[7]);
		mPlanes[2].set(mPlanePoints[0], mPlanePoints[4], mPlanePoints[3]);
		mPlanes[3].set(mPlanePoints[5], mPlanePoints[1], mPlanePoints[6]);
		mPlanes[4].set(mPlanePoints[2], mPlanePoints[3], mPlanePoints[6]);
		mPlanes[5].set(mPlanePoints[4], mPlanePoints[0], mPlanePoints[1]);
	}    

	public boolean pointInFrustum (Vector3 point) {
		for (int i = 0; i < mPlanes.length; i++) {
			PlaneSide result = mPlanes[i].getPointSide(point);
			if (result == PlaneSide.Back) {return false;}
		}
		return true;
	}

	public void calculateBounds(Geometry3D geometry) {
		RajLog.w("[" + this.getClass().getName()
				+ "] The method CameraFrustum#calculateBounds(Geometry3D) does nothing. You should remove your call to it.");
	}

	float[] mMMatrix = new float[16];
	float[] mRotateMatrix = new float[16];
	float[] mTempOffset = new float[]{0, 0, 0, 1};
	float[] mResultVec = new float[4];
	Vector3 mTempPosition = new Vector3();
	
	public void drawBoundingVolume(Camera camera, float[] vpMatrix, float[] projMatrix, float[] vMatrix, float[] mMatrix) {
		/*if(mVisibleFrustum == null) {
			mVisibleFrustum = new CameraVisibleFrustum(this, 4, 1, 3, 5);
			mVisibleFrustum.setMaterial(new SimpleMaterial());
			mVisibleFrustum.getMaterial().setUseSingleColor(true);
			mVisibleFrustum.setColor(mBoundingColor.get());
			mVisibleFrustum.setDrawingMode(GLES20.GL_LINE_LOOP);
		}
		mVisibleFrustum.setColor(0xFF00FF00);
		mVisibleFrustum.update(true);
		
		Matrix.setIdentityM(mMMatrix, 0);
		Matrix.setIdentityM(mRotateMatrix, 0);

		mTempPosition.setAll(mCamera.getPosition());
		double offset = mVisibleFrustum.getRadiusTop() / Math.tan(mCamera.getFieldOfView()*Math.PI/360.0);
		mTempOffset[0] = 0; mTempOffset[2] = 0; mTempOffset[3] = 0;
		mTempOffset[1] = (float) (offset + mVisibleFrustum.getHeight()/2.0);
		
		if (mCamera.getLookAt() == null) {
			RajLog.d("Using quaternion orientation.");
			mCamera.getOrientation(mOrientation);
			mOrientation.inverse().multiplyLeft(s90_DEGREE);
			mVisibleFrustum.setOrientation(mOrientation);
			mOrientation.toRotationMatrix(mRotateMatrix);
			
			Matrix.multiplyMV(mResultVec, 0, mRotateMatrix, 0, mTempOffset, 0);
			mTempPosition.x -= mResultVec[0];
			mTempPosition.y -= mResultVec[1];
			mTempPosition.z -= mResultVec[2];
			mVisibleFrustum.setPosition(mTempPosition);
		} else {
			RajLog.d("Using mLookAt orientation.");
			mVisibleFrustum.setLookAt(mCamera.getLookAt());
			mCamera.getOrientation(mOrientation);
			System.arraycopy(mCamera.getLookAtMatrix(), 0, mRotateMatrix, 0, 16);
			
			RajLog.v("Camera LookAt Orientation: " + mOrientation);
			//mOrientation.inverse().multiplyLeft(s90_DEGREE);
			mVisibleFrustum.setOrientation(mOrientation);
			mOrientation.toRotationMatrix(mRotateMatrix);
		}*/
		//TODO: Uncomment this when everything works.
		//mVisibleFrustum.render(camera, vpMatrix, projMatrix, vMatrix, null, null);
	}

	public void transform(float[] matrix) {
		RajLog.i("[" + this.getClass().getName()
				+ "] The method CameraFrustum#transform(float[]) does nothing. You should remove your call to it.");
	}

	public BaseObject3D getVisual() {
		return mVisibleFrustum;
	}

	public void setBoundingColor(int color) {
		mBoundingColor.set(color);
	}

	public int getBoundingColor() {
		return mBoundingColor.get();
	}
	
	protected static class CameraVisibleFrustum extends NPrism {

		private CameraFrustum mParent;
		
		public CameraVisibleFrustum(CameraFrustum parent, int sides, double radiusTop, double radiusBase, double height) {
			super(sides, radiusTop, radiusBase, height);
			mParent = parent;
		}
		
		private double getHeight() {
			return mHeight;
		}
		
		private double getRadiusTop() {
			return mRadiusTop;
		}
		
		private void update(boolean update) {
			double near, far;
			Vector3 pos_corner = mParent.mPlanePoints[2];
			Vector3 neg_corner = mParent.mPlanePoints[0];
			mRadiusTop = (pos_corner.x - neg_corner.x)*ROOT2_4;
			mMinorTop = (pos_corner.y - neg_corner.y)*ROOT2_4;
			near = mParent.mCamera.getNearPlane();
			pos_corner = mParent.mPlanePoints[6];
			neg_corner = mParent.mPlanePoints[4];
			mRadiusBase = (pos_corner.x - neg_corner.x)*ROOT2_4;
			mMinorBase = (pos_corner.y - neg_corner.y)*ROOT2_4;
			far = mParent.mCamera.getFarPlane();
			double major_squared = Math.pow(mRadiusBase, 2.0);
			double minor_squared = Math.pow(mMinorBase, 2.0);
			mEccentricity = Math.sqrt((major_squared-minor_squared)/major_squared);
			mHeight = Math.abs(far - near);
			init(update);
		}
	}
}