export type PlaywrightFile = {
  name: string;
  mimeType: string;
  buffer: Buffer;
};

export const SmallImageFile: PlaywrightFile = {
  buffer: Buffer.from(
    'iVBORw0KGgoAAAANSUhEUgAAAAEAAAABAQAAAAA3bvkkAAAAAmJLR0QAAd2KE6QAAAAKSURBVAjXY2gAAACCAIHdQ2r0AAAAAElFTkSuQmCC',
    'base64',
  ),
  mimeType: 'image/png',
  name: 'small.png',
};
