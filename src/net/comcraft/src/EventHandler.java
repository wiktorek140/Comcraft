package net.comcraft.src;

import java.util.Hashtable;
import java.util.Vector;

import com.google.minijoe.sys.JsArray;
import com.google.minijoe.sys.JsException;
import com.google.minijoe.sys.JsFunction;
import com.google.minijoe.sys.JsObject;

public class EventHandler {

    private Hashtable events;

    public EventHandler(String[] eventnames) {
        this();
        for (int i = 0; i < eventnames.length; i++) {
            addEventName(eventnames[i]);
        }
    }

    public EventHandler() {
        events = new Hashtable();
    }

    protected void addEventName(String name) {
        Vector[] v = new Vector[2];
        v[0] = new Vector();
        v[1] = new Vector();
        events.put(name, v);
    }

    public boolean hasEvent(String name) {
        return events.containsKey(name);
    }

    public void runEvent(String name, Object[] params) {
        runEvent(name, null, params);
    }

    public void runEvent(String name, JsObject thisPtr, Object[] params) {
        if (params == null) {
            params = new Object[0];
        }
        Vector[] event = (Vector[]) events.get(name);
        if (event[0].isEmpty()) {
            return;
        }
        Vector e = event[0];
        JsArray stack = new JsArray();
        for (int ce = 0; ce < e.size(); ce++) {
            JsFunction fn = (JsFunction) e.elementAt(ce);
            stack.setObject(0, thisPtr != null ? thisPtr : fn); // context ('this' variable)
            stack.setObject(1, fn); // Function
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof Integer) {
                    stack.setInt(i + 2, ((Integer) params[i]).intValue());
                } else if (params[i] instanceof Boolean) {
                    stack.setBoolean(i + 2, ((Boolean) params[i]).booleanValue());
                } else {
                    stack.setObject(i + 2, params[i]);
                }
            }
            try {
                fn.eval(stack, 0, params.length);
                event[1].setElementAt(stack.getObject(0), ce);
            } catch (Exception e1) {
                System.err.println(e1.getMessage());
                e1.printStackTrace();
            }
        }
    }

    public void runEvent(String name) {
        runEvent(name, null);
    }

    public void bindEvent(String name, JsFunction function) {
        if (name == null || function == null) {
            return;
        }
        if (!events.containsKey(name)) {
            throw new JsException("Unknown Event key " + name);
        }
        Vector[] e = (Vector[]) events.get(name);
        e[0].addElement(function);
        e[1].addElement(null);
    }

    public void setEvent(String name, JsFunction function) {
        if (name == null || function == null) {
            return;
        }
        if (!events.containsKey(name)) {
            throw new JsException("Unknown Event key " + name);
        }
        Vector[] e = (Vector[]) events.get(name);
        e[0].removeAllElements();
        e[1].removeAllElements();
        e[0].addElement(function);
        e[1].addElement(null);
    }

    public Object getLastReturn(String name) {
        return ((Vector[]) events.get(name))[1].lastElement();
    }

    public Object getLastSuccess(String name) {
        Vector event = ((Vector[]) events.get(name))[1];
        Object r = null;
        for (int i = 0; i < event.size(); i++) {
            Object t = event.elementAt(i);
            if (t != null) {
                r = t;
            }
        }
        return r;
    }

    public Object getFirstSuccess(String name) {
        Vector event = ((Vector[]) events.get(name))[1];
        for (int i = 0; i < event.size(); i++) {
            Object r = event.elementAt(i);
            if (r != null) {
                return r;
            }
        }
        return null;
    }

    public Object[] getSucesses(String name) {
        Vector event = ((Vector[]) events.get(name))[1];
        Vector v = new Vector();
        for (int i = 0; i < event.size(); i++) {
            Object r = event.elementAt(i);
            if (r != null) {
                v.addElement(r);
            }
        }
        Object[] arr = new Object[v.size()];
        v.copyInto(arr);
        return arr;
    }
}
