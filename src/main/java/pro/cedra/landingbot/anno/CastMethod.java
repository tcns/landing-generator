package pro.cedra.landingbot.anno;

import java.util.function.Function;

/**
 * Created by TIMUR on 18.11.2017.
 */
public enum CastMethod {
    FLOAT {
        @Override
        public Function<String, Object> cast() {
            return s -> Float.parseFloat(s);
        }
    },
    INTEGER {
        @Override
        public Function<String, Object> cast() {
            return s -> Integer.parseInt(s);
        }
    },
    STRING {
        @Override
        public Function<String, Object> cast() {
            return s -> s;
        }
    },
    BOOLEAN {
        @Override
        public Function<String, Object> cast() {
            return s -> "Y".equals(s.toUpperCase());
        }
    };
    public abstract Function<String, Object> cast();
}
