import { readFileSync, writeFileSync, mkdirSync, existsSync } from "fs";
import { parseArgs } from "util";

const options = {
  filter: {
    type: "string",
    short: "f",
  },
};

const args = parseArgs({ options }).values;

const filterDef = JSON.parse(readFileSync(args.filter));
const filters = filterDef.filters;

const psfs = "public static final String";
const result = [];

for (const type of filters) {
  for (const requestedRole of type.roles) {
    result.push(`${psfs} ${type.postfix}_${requestedRole} = "${filterDef.prefix}_${requestedRole}_${type.postfix}";`);
  }

  result.push("");
}

const output = `
public final class OidcPermissions {
    ${result.join("\n    ")}
}
`;

if (!existsSync("out")) {
  mkdirSync("out");
}
writeFileSync("./out/OidcPermissions.java", output);
