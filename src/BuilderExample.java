public class BuilderExample {
    private final String message;
    private final boolean dryRun;

    public BuilderExample(Builder builder) {
        this.message = builder.message;
        this.dryRun = builder.dryRun;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getMessage() {
        return dryRun ? message : "restricted";
    }

    public static class Builder {
        protected String message;
        protected boolean dryRun;

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder dryRun(boolean dryRun) {
            this.dryRun = dryRun;
            return this;
        }

        public BuilderExample build() {
            return new BuilderExample(this);
        }

    }
}
