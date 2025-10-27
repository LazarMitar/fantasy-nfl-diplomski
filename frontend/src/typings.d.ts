declare module 'country-list' {
    export function getData(): { code: string; name: string }[];
    export function getName(code: string): string;
    export function getCode(name: string): string;
    export function getNames(): string[];
    export function getCodes(): string[];
  }