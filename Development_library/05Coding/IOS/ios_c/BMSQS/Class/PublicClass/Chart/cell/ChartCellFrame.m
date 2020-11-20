//
//  ChartCellFrame.m
//  气泡
//
//  Created by zzy on 14-5-13.
//  Copyright (c) 2014年 zzy. All rights reserved.
//
#define kIconMarginX 5
#define kIconMarginY 35

#import "ChartCellFrame.h"

@implementation ChartCellFrame

-(void)setChartMessage:(ChartMessage *)chartMessage
{
    _chartMessage=chartMessage;
    
    CGSize winSize=[UIScreen mainScreen].bounds.size;
    CGFloat iconX=kIconMarginX;
    CGFloat iconY=kIconMarginY;
    CGFloat iconWidth=40;
    CGFloat iconHeight=40;
    
    if(chartMessage.messageType==kMessageFrom){
      
    }else if (chartMessage.messageType==kMessageTo){
        iconX=winSize.width-kIconMarginX-iconWidth;
    }
    self.iconRect=CGRectMake(iconX, iconY, iconWidth, iconHeight);
    
    CGFloat contentX=CGRectGetMaxX(self.iconRect)+kIconMarginX;
    CGFloat contentY=iconY;
    NSDictionary *attributes = @{NSFontAttributeName: [UIFont systemFontOfSize:13.f]};
    CGSize contentSize=[chartMessage.content boundingRectWithSize:CGSizeMake(200, MAXFLOAT) options: NSStringDrawingTruncatesLastVisibleLine | NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading attributes:attributes context:nil].size;
    if(chartMessage.messageType==kMessageTo){
    
        contentX=iconX-kIconMarginX-contentSize.width-iconWidth;
    }
    if(_chartMessage.dict&&[_chartMessage.dict objectForKey:@"bestanswer"]&&[[_chartMessage.dict objectForKey:@"bestanswer"] intValue]==1){
        self.chartViewRect=CGRectMake(contentX, contentY, contentSize.width+35+30, contentSize.height+40);
        self.cellHeight=MAX(CGRectGetMaxY(self.iconRect), CGRectGetMaxY(self.chartViewRect))+kIconMarginX+40;

    }else{
        self.chartViewRect=CGRectMake(contentX, contentY, contentSize.width+35, contentSize.height+30);
        self.cellHeight=MAX(CGRectGetMaxY(self.iconRect), CGRectGetMaxY(self.chartViewRect))+kIconMarginX;

    }
    //self.chartViewRect=CGRectMake(contentX, contentY, contentSize.width+35, contentSize.height+30);

}
@end
