import { readFileSync, writeFileSync, mkdirSync, existsSync } from "fs";
import { parseArgs } from "util";

const copyWithNewName = (input, postfix) => {
  const copy = structuredClone(input);
  copy.name = `${filterDef.prefix}_${postfix}_${input.name}`;
  return copy;
};

const copyCompositeWithNameAndRoles = (template, name, roles) => {
  const copy = structuredClone(template);
  copy.name = name;
  copy.composites.realm = roles;
  return copy;
};

const options = {
  filter: {
    type: "string",
    short: "f",
  },
  realm: {
    type: "string",
    short: "r",
  },
};

const args = parseArgs({ options }).values;

const filterDef = JSON.parse(readFileSync(args.filter));
const filters = filterDef.filters;

const realmDump = JSON.parse(readFileSync(args.realm));
const existingRoles = realmDump.roles.realm;

const result = {
  roles: {
    realm: [],
  },
};

const compositeTemplate = existingRoles.find(
  (existingRole) => existingRole.composite
);
if (!compositeTemplate) {
  console.log("No composite role was found, skipping creation");
}

for (const type of filters) {
  const roleNamesOfType = [];

  for (const requestedRole of type.roles) {
    for (const role of existingRoles) {
      if (requestedRole == role.name) {
        const copy = copyWithNewName(role, type.postfix);
        result.roles.realm.push(copy);
        roleNamesOfType.push(copy.name);
      }
    }
  }

  if (compositeTemplate) {
    result.roles.realm.push(
      copyCompositeWithNameAndRoles(
        compositeTemplate,
        type.composite,
        roleNamesOfType
      )
    );
  }
}

if (!existsSync("out")) {
  mkdirSync("out");
}
writeFileSync("./out/generated_realm.json", JSON.stringify(result, null, 2));
