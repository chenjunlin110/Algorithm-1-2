/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;
    private double[][] energyArray;
    private boolean istranspsoed;
    private boolean isHorizontalSeamCall;

    public SeamCarver(Picture picture) {
        this.picture = picture;
        energyArray = energy();
        isHorizontalSeamCall = false;
        istranspsoed = false;
    }

    private double[][] energy() {
        double[][] energyArray = new double[this.height()][this.width()];
        for (int i = 0; i < this.height(); i++) {
            for (int j = 0; j < this.width(); j++) {
                energyArray[i][j] = energy(j, i);
            }
        }
        return energyArray;
    }


    public Picture picture() {
        return picture;
    }

    public int width() {
        return picture.width();
    }

    public int height() {
        return picture.height();
    }

    public double energy(int x, int y) {
        if (x < 0 || x >= this.width() || y < 0 || y >= this.height())
            throw new IllegalArgumentException("Invalid energy call.");
        if (x == 0 || x == this.width() - 1 || y == 0 || y == this.height() - 1) return 1000;
        int rgbleft = this.picture.getRGB(x - 1, y);
        int rgbright = this.picture.getRGB(x + 1, y);
        int rgbup = this.picture.getRGB(x, y - 1);
        int rgbdown = this.picture.getRGB(x, y + 1);

        int rdx = ((rgbleft >> 16) & 0xFF) - ((rgbright >> 16) & 0xFF);
        int gdx = ((rgbleft >> 8) & 0xFF) - ((rgbright >> 8) & 0xFF);
        int bdx = ((rgbleft) & 0xFF) - ((rgbright) & 0xFF);

        int rdy = ((rgbup >> 16) & 0xFF) - ((rgbdown >> 16) & 0xFF);
        int gdy = ((rgbup >> 8) & 0xFF) - ((rgbdown >> 8) & 0xFF);
        int bdy = ((rgbup) & 0xFF) - ((rgbdown) & 0xFF);

        int delx = rdx * rdx + gdx * gdx + bdx * bdx;
        int dely = rdy * rdy + gdy * gdy + bdy * bdy;

        return Math.sqrt(delx + dely);
    }

    private void transpsoedImage() {
        Picture newpic = new Picture(this.height(), this.width());
        double[][] newenergy = new double[this.width()][this.height()];
        for (int i = 0; i < this.width(); i++) {
            for (int j = 0; j < this.height(); j++) {
                newpic.set(j, i, this.picture.get(i, j));
                newenergy[i][j] = this.energyArray[j][i];
            }
        }

        this.energyArray = newenergy;
        this.picture = newpic;
        if (this.istranspsoed) this.istranspsoed = false;
        else istranspsoed = true;
    }

    public int[] findVerticalSeam() {
        if (istranspsoed && !isHorizontalSeamCall) transpsoedImage();
        Double[][] temparray = new Double[this.height()][this.width()];
        for (int j = 0; j < this.height(); j++) {
            for (int i = 0; i < this.width(); i++) {
                if (j == 0) temparray[j][i] = 1000.0;
                else temparray[j][i] = Double.MAX_VALUE;
            }
        }
        double sum1;
        double sum2;
        double sum3;

        for (int j = 1; j < this.height(); j++) { // iterate form second row to end
            for (int i = 0; i < this.width(); i++) { // iterate rows from border to border

                if (i > 0) {
                    sum1 = energyArray[j][i - 1] + temparray[j - 1][i];
                    if (sum1 < temparray[j][i - 1]) temparray[j][i - 1] = sum1;
                }

                sum2 = energyArray[j][i] + temparray[j - 1][i];
                if (sum2 < temparray[j][i]) temparray[j][i] = sum2;

                if (i < this.width() - 1) {
                    sum3 = energyArray[j][i + 1] + temparray[j - 1][i];
                    if (sum3 < temparray[j][i + 1]) temparray[j][i + 1] = sum3;
                }

            }
        }

        int minindex = 0;
        double min = Double.MAX_VALUE;
        int l = this.height() - 1;
        for (int i = 1; i < this.width() - 1; i++) {
            if (temparray[l][i] < min) {
                min = temparray[l][i];
                minindex = i;
            }
        }

        int[] verticalroute = new int[this.height()];
        verticalroute[this.height() - 1] = minindex;
        int tempminindex = 0;
        for (int j = this.height() - 2; j > -1; j--) {
            min = Double.MAX_VALUE;
            tempminindex = minindex;
            if (tempminindex > 0) {
                if (temparray[j][tempminindex - 1] < min) {
                    min = temparray[j][tempminindex - 1];
                    minindex = tempminindex - 1;
                }
            }

            if (temparray[j][tempminindex] < min) {
                min = temparray[j][tempminindex];
                minindex = tempminindex;
            }

            if (tempminindex < this.width() - 1) {
                if (temparray[j][tempminindex + 1] < min) {
                    min = temparray[j][tempminindex + 1];
                    minindex = tempminindex + 1;
                }
            }

            verticalroute[j] = minindex;
        }
        return verticalroute;
    }


    public int[] findHorizontalSeam() {
        if (!this.istranspsoed) {
            transpsoedImage();
        }
        this.isHorizontalSeamCall = true;
        int[] seam = findVerticalSeam();
        this.isHorizontalSeamCall = false;
        transpsoedImage();
        this.istranspsoed = false;
        return seam;
    }

    private boolean validSeam(int[] seam) {
        if (seam == null) return false;
        if (this.width() <= 1) return false;
        if (seam.length != this.height()) return false;
        if (seam[0] < 0 || seam[0] >= this.width()) return false;
        for (int i = 1; i < seam.length; i++) {
            if ((seam[i - 1] - seam[i]) * (seam[i - 1] - seam[i]) > 1) return false;
            if (seam[i] < 0 || seam[i] >= this.width()) return false;
        }
        return true;
    }

    public void removeVerticalSeam(int[] seam) {
        if (!validSeam(seam)) throw new IllegalArgumentException();
        if (istranspsoed && !isHorizontalSeamCall) transpsoedImage();
        int j = 0;
        Picture newpic = new Picture(this.width() - 1, this.height());
        double[][] newenergy = new double[this.height()][width() - 1];
        for (int i : seam) {
            for (int k = 0; k < newpic.width(); k++) {
                if (k < i) newpic.set(k, j, this.picture.get(k, j));
                else newpic.set(k, j, this.picture.get(k + 1, j));
            }
            System.arraycopy(energyArray[j], 0, newenergy[j], 0, i);
            System.arraycopy(energyArray[j], i + 1, newenergy[j], i, this.width() - 1 - i);
            j++;
        }
        this.picture = newpic;
        this.energyArray = newenergy;

        int j1 = 0;
        for (int i : seam) {
            if (i != 0)
                this.energyArray[j1][i - 1] = energy(i - 1, j1);
            if (i != this.width())
                this.energyArray[j1][i] = energy(i, j1);
        }
    }

    public void removeHorizontalSeam(int[] seam) {
        if (!istranspsoed) transpsoedImage();
        this.isHorizontalSeamCall = true;
        removeVerticalSeam(seam);
        this.isHorizontalSeamCall = false;
        transpsoedImage();
        istranspsoed = false;
    }

    public static void main(String[] args) {

    }
}
