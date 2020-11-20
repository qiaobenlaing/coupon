//
//  ActImageTextAttachment.m
//  BMSQS
//
//  Created by gh on 16/1/24.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "ActImageTextAttachment.h"

@implementation ActImageTextAttachment

- (CGRect)attachmentBoundsForTextContainer:(NSTextContainer *)textContainer proposedLineFragment:(CGRect)lineFrag glyphPosition:(CGPoint)position characterIndex:(NSUInteger)charIndex {
    if (_imgframe.size.height) {
        return _imgframe;
        
    }
    
    return CGRectMake(0, 0, _imgSize.width, _imgSize.height);
        
    
    
    
    
}


@end
