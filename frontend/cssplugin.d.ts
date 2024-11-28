import { PluginCreator } from 'tailwindcss/types/config';

// https://github.com/tailwindlabs/tailwindcss-intellisense/issues/227#issuecomment-2344596934

declare const e: (filename: string) => PluginCreator;

export = e;
