variableList = ["versionName", "libraryName", "libraryGroup"]

def get_variables():
    variables = {}

    f = open("gradle.properties", "rt")
    for line in f.readlines():
        t = [v  for v in variableList if (v in line)]
        if len(t) > 0:
            key = t[0]
            value = line.strip().replace(key  + "=", '')
            variables[key] = value

    return variables

def generate_readme():
    variables = get_variables()

    # read file and replace variables
    input = open("README_raw.md", "rt")
    data = input.read()
    for key in variables:
        v = "{{" + key + "}}"
        data = data.replace(v, variables[key])
    input.close()

    # save result file
    output = open("README.md", "wt")
    output.write(data)
    output.close()

    print("README.md created successfully !!")


if __name__ == '__main__':
    generate_readme()
