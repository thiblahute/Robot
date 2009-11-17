/** 
 * House for android 1.5
 *
 * @Copyright (C) Thibault Saunier 2009 <saunierthibault@gmail.com>
 *
 * Based on several tutorial under apache licence accessible at:
 * <url>http://developer.android.com/guide/samples</url>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @Author: Thibault Saunier <saunierthibault@gmail.com>
 */

package upla.multimedia.robot;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

/** This class permit to describe the base cuerpo of the robot
 *  Basicly, it is a green rectangle cube.
 **/ 
class Cuerpo
{
    public Cuerpo()
      {
        /* Base size */
        int one = 0x9999;

        /* Vertice initialization */
        int vertices[] = {
            -one, -one, -one*2,
            one, -one, -one*2,
            one,  one, -one*2,
            -one,  one, -one*2,

            -one, -one,  one,
            one, -one,  one,
            one,  one,  one,
            -one,  one,  one,
        };

        /* Color initialization */
        int colors[] = {
            0, one, 0, one,
            0, one, 0, one,
            0, one, 0, one,
            0, one, 0, one,
            0, one, 0, one,
            0, one, 0, one,
            0, one, 0, one,
            0, one, 0, one,
        };

        /* TODO figure out how it works exactly */
        byte indices[] = {
            0, 4, 5,    0, 5, 1,
            1, 5, 6,    1, 6, 2,
            2, 6, 7,    2, 7, 3,
            3, 7, 4,    3, 4, 0,
            4, 7, 6,    4, 6, 5,
            3, 0, 1,    3, 1, 2
        };


        /* Cuerpo buffer */
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asIntBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        /* Color buffer */
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder()); /*TODO */
        mColorBuffer = cbb.asIntBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
      }

    public void draw(GL10 gl)
      {
        gl.glFrontFace(gl.GL_CW);
        gl.glVertexPointer(3, gl.GL_FIXED, 0, mVertexBuffer);
        gl.glColorPointer(4, gl.GL_FIXED, 0, mColorBuffer);
        gl.glDrawElements(gl.GL_TRIANGLES, 36, gl.GL_UNSIGNED_BYTE, mIndexBuffer);
      }

    private IntBuffer   mVertexBuffer; /* Contains the vertex buffer */
    private IntBuffer   mColorBuffer; /* Contains the color buffer */
    private ByteBuffer  mIndexBuffer;
}
