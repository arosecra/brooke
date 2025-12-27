export function flatten(node: any, pluckMember: string, childrenMember: string) {
  const res = [];
  const pluckValue = node[pluckMember];
  if (pluckValue) res.push(pluckValue);
  const children = node[childrenMember];
  if (children) {
    const childValues = children.map((child: any) => flatten(child, pluckMember, childrenMember));
    if (childValues) res.push(...childValues);
  }
  return res;
}
