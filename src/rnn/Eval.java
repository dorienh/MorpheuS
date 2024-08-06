/**
 * Created by dorien on 06/08/15.
 */

package rnn;

public enum Eval {
    Min, Profile;

        public static Eval parse(String s) {
            for (Eval eval : values()) {
                if (eval.name().equals(s)) {
                    return eval;
                }
            }
            throw new IllegalArgumentException("Unknown eval type [" + s + "].");
        }
    }


