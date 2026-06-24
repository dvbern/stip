import {
  TimelineBusyBlock,
  TimelineGapBlock,
  TimelineRawItem,
  TwoColumnTimeline,
} from './two-column-timeline';

describe('TwoColumnTimeline', () => {
  const testItems = {
    EFZ: {
      id: '11',
      col: 'LEFT',
      label: { type: 'TEXT', title: 'Informatiker EFZ' },
      von: new Date(2016, 7),
      bis: new Date(2017, 11),
      invalid: false,
      editable: true,
      ausbildungAbgeschlossen: false,
    },
    JobA: {
      id: '12',
      col: 'RIGHT',
      label: { type: 'TEXT', title: 'Job A' },
      von: new Date(2016, 7),
      bis: new Date(2016, 9),
      invalid: false,
      editable: true,
      ausbildungAbgeschlossen: false,
    },
    JobB: {
      id: '13',
      col: 'RIGHT',
      label: { type: 'TEXT', title: 'Job B' },
      von: new Date(2017, 7),
      bis: new Date(2017, 10),
      invalid: false,
      editable: true,
      ausbildungAbgeschlossen: false,
    },
    JobC: {
      id: '14',
      col: 'RIGHT',
      label: { type: 'TEXT', title: 'Job C' },
      von: new Date(2017, 8),
      bis: new Date(2017, 8),
      invalid: false,
      editable: true,
      ausbildungAbgeschlossen: false,
    },
    JobD: {
      id: '15',
      col: 'RIGHT',
      label: { type: 'TEXT', title: 'Job D' },
      von: new Date(2017, 10),
      bis: new Date(2017, 11),
      invalid: false,
      editable: true,
      ausbildungAbgeschlossen: false,
    },
  } satisfies Record<string, TimelineRawItem>;
  const ausbildungItem: TimelineRawItem = {
    id: 'planned-ausbildung',
    col: 'LEFT',
    label: { type: 'TEXT', title: 'Ausbildung' },
    von: new Date(2017, 12),
    bis: new Date(2021, 11),
    invalid: false,
    editable: false,
    ausbildungAbgeschlossen: false,
  };
  const [dummyItems, dummyAusbildung] = createDummyItems();

  beforeEach(() => {
    vitest.spyOn(console, 'log').mockImplementation(() => {
      // do nothing
    });
  });

  it('should fillWith correctly on empy', () => {
    const result = new TwoColumnTimeline();
    result.fillWith(new Date(2016, 1), [], ausbildungItem);
    expect(result.leftCols).toEqual(1);
    expect(result.rightCols).toEqual(1);
  });

  it('should fillWith correctly like dummyItems', () => {
    const result = new TwoColumnTimeline();
    result.fillWith(
      new Date(2016, 1),
      [
        testItems.EFZ,
        testItems.JobA,
        testItems.JobB,
        testItems.JobC,
        testItems.JobD,
      ],
      ausbildungItem,
    );
    expect(result.items).toStrictEqual([...dummyItems, dummyAusbildung]);
  });

  function createDummyItems(): [
    (TimelineBusyBlock | TimelineGapBlock)[],
    TimelineBusyBlock,
  ] {
    let leftIndex = 1;
    let rightIndex = 1;

    const items: (TimelineBusyBlock | TimelineGapBlock)[] = [];

    items.push(
      // gap
      {
        col: 'BOTH',
        von: new Date(2016, 1),
        bis: new Date(2016, 6),
        positionStartRow: leftIndex,
        positionRowSpan: 1,
        positionStartCol: 1,
        positionColSpan: 2,
      } as TimelineGapBlock,
    );
    leftIndex += 1;
    rightIndex += 1;

    items.push(
      // job
      {
        ...testItems.JobA,
        positionStartRow: rightIndex,
        positionRowSpan: 1,
        positionStartCol: 2,
        positionColSpan: 1,
        ausbildungAbgeschlossen: false,
        children: [testItems.JobA],
      },
    );
    rightIndex += 1;

    items.push(
      // ausbildung
      {
        ...testItems.EFZ,
        positionStartRow: leftIndex,
        positionRowSpan: 5,
        positionStartCol: 1,
        positionColSpan: 1,
        ausbildungAbgeschlossen: false,
        children: [testItems.EFZ],
      },
    );
    rightIndex += 1;

    items.push(
      // job
      {
        ...testItems.JobB,
        positionStartRow: rightIndex,
        positionRowSpan: 2,
        positionStartCol: 2,
        positionColSpan: 1,
        ausbildungAbgeschlossen: false,
        children: [testItems.JobB, testItems.JobC],
      },
    );
    rightIndex += 1;

    items.push(
      // job
      {
        ...testItems.JobD,
        positionStartRow: rightIndex,
        positionRowSpan: 2,
        positionStartCol: 2,
        positionColSpan: 1,
        ausbildungAbgeschlossen: false,
        children: [testItems.JobD],
      },
    );

    return [
      items,
      {
        ...ausbildungItem,
        positionStartRow: 7,
        positionRowSpan: 1,
        positionStartCol: 1,
        positionColSpan: 1,
        ausbildungAbgeschlossen: false,
        children: [ausbildungItem],
      },
    ];
  }
});
