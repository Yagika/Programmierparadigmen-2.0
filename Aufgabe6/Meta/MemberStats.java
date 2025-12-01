package Meta;

@Responsible("Aleksander")
class MemberStats {
    int components;
    int methodsAndConstructors;
    int assertions;

    void incComponents() {
        components++;
    }

    void addMethodsAndConstructors(int count) {
        methodsAndConstructors += count;
    }

    void addAssertions(int count) {
        assertions += count;
    }
}
