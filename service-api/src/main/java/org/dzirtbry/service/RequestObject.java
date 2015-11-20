package org.dzirtbry.service;

/**
 * Created on 11/20/15.
 *
 * @author dzirtbry
 */
public class RequestObject {
    private String test1;
    private int test2;

    public RequestObject() {
    }

    public String getTest1() {
        return test1;
    }

    public void setTest1(String test1) {
        this.test1 = test1;
    }

    public int getTest2() {
        return test2;
    }

    public void setTest2(int test2) {
        this.test2 = test2;
    }

    @Override
    public String toString() {
        return "RequestObject{" +
                "test1='" + test1 + '\'' +
                ", test2=" + test2 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequestObject that = (RequestObject) o;

        if (test2 != that.test2) return false;
        return !(test1 != null ? !test1.equals(that.test1) : that.test1 != null);

    }

    @Override
    public int hashCode() {
        int result = test1 != null ? test1.hashCode() : 0;
        result = 31 * result + test2;
        return result;
    }
}
